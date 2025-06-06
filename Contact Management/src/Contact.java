public class Contact {
    String name,phone,email;

    public Contact(String name, String phone,String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
    @Override

    public String toString()//If someone says System.out.println(contact), what should we print?”
    {
        return name + " | " + phone + " | " + email;
    }


}
