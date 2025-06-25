package service;

import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionService {

    public boolean issueBook(int bookId, int studentId, String dueDate) {
        String checkSql = "SELECT is_borrowed FROM books WHERE id = ?";
        String issueSql = "INSERT INTO transactions(book_id, student_id, issue_date, due_date, status) VALUES (?, ?, date('now'), ?, 'issued')";
        String updateBookStatus = "UPDATE books SET is_borrowed = 1 WHERE id = ?";

        try (Connection conn = DBUtil.getConnection()) {
            // Step 1: Check if the book is already issued
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, bookId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt("is_borrowed") == 1) {
                    return false; // Already issued
                }
            }

            // Step 2: Insert transaction and update status
            try (
                    PreparedStatement pst1 = conn.prepareStatement(issueSql);
                    PreparedStatement pst2 = conn.prepareStatement(updateBookStatus)
            ) {
                pst1.setInt(1, bookId);
                pst1.setInt(2, studentId);
                pst1.setString(3, dueDate);
                pst1.executeUpdate();

                pst2.setInt(1, bookId);
                pst2.executeUpdate();

                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public boolean returnBook(int bookId, int studentId) {
        String returnSql = "UPDATE transactions SET return_date = date('now'), status = 'returned' " +
                "WHERE book_id = ? AND student_id = ? AND status = 'issued'";
        String updateBookSql = "UPDATE books SET is_borrowed = 0 WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pst1 = conn.prepareStatement(returnSql);
             PreparedStatement pst2 = conn.prepareStatement(updateBookSql)) {

            pst1.setInt(1, bookId);
            pst1.setInt(2, studentId);
            int rowsAffected = pst1.executeUpdate();

            if (rowsAffected > 0) {
                pst2.setInt(1, bookId);
                pst2.executeUpdate();
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    public int countBorrowedBooks() {
        String sql = "SELECT COUNT(*) FROM transactions WHERE status = 'issued'";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pst = conn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countReturnedBooks() {
        String sql = "SELECT COUNT(*) FROM transactions WHERE status = 'returned'";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pst = conn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Object[]> getIssueDetails() {
        List<Object[]> list = new ArrayList<>();
        String sql = """
            SELECT s.id as student_id, s.name, b.id as book_id, b.title, t.issue_date, t.return_date
            FROM transactions t
            JOIN students s ON t.student_id = s.id
            JOIN books b ON t.book_id = b.id
            WHERE t.status = 'issued'
        """;
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pst = conn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("issue_date"),
                        rs.getString("return_date") != null ? rs.getString("return_date") : "N/A"
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Object[]> getReturnDetails() {
        List<Object[]> list = new ArrayList<>();
        String sql = """
            SELECT s.id as student_id, s.name, b.id as book_id, b.title, t.issue_date, t.return_date
            FROM transactions t
            JOIN students s ON t.student_id = s.id
            JOIN books b ON t.book_id = b.id
            WHERE t.status = 'returned'
        """;
        try (Connection conn = DBUtil.getConnection(); PreparedStatement pst = conn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("issue_date"),
                        rs.getString("return_date")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
