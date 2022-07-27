import java.sql.*;

public class Customer {
    private static String tableName = "customers";
    private int id;
    private String username;
    private String last_name;
    private String first_name;
    private String phone;
    private String address;
    private String city;
    private String postal_code;
    private String country;

    public Customer(int id,
                    String username,
                    String last_name,
                    String first_name,
                    String phone,
                    String address,
                    String city,
                    String postal_code,
                    String country) {
        this.id = id;
        this.username = username;
        this.last_name = last_name;
        this.first_name = first_name;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.postal_code = postal_code;
        this.country = country;
    }
    //******************************************************************************************************************
    //GENERATED GETTERS AND SETTERS

    public static String getTableName() {
        return tableName;
    }

    public static void setTableName(String tableName) {
        Customer.tableName = tableName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postal_code;
    }

    public void setPostalCode(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", last_name='" + last_name + '\'' +
                ", first_name='" + first_name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", postal_code='" + postal_code + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
    //******************************************************************************************************************


    //Get Customer with Id given

    public static Customer getById(Connection connection, int id) throws SQLException {
        Statement s = connection.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + Customer.tableName + " WHERE `id` = " + id + " LIMIT 1");

        if (rs.next()) {
            int selId = rs.getInt("id");
            String username = rs.getString("username");
            String lastName = rs.getString("last_name");
            String firstName = rs.getString("first_name");
            String phone = rs.getString("phone");
            String address = rs.getString("address");
            String city = rs.getString("city");
            String postalCode = rs.getString("postal_code");
            String country = rs.getString("country");
            return new Customer(
                    selId,
                    username,
                    lastName,
                    firstName,
                    phone,
                    address,
                    city,
                    postalCode,
                    country
            );
        }
        return null;
    }


    //Insert new Customer in DataBase

    public static void insertCustomer(Connection connection, Customer newCustomer) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO " + Customer.tableName +
                            " (username, last_name, first_name, phone, address, city, postal_code, country)" +
                            " VALUES (? , ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, newCustomer.getUsername());
            ps.setString(2, newCustomer.getLastName());
            ps.setString(3, newCustomer.getFirstName());
            ps.setString(4, newCustomer.getPhone());
            ps.setString(5, newCustomer.getAddress());
            ps.setString(6, newCustomer.getCity());
            ps.setString(7, newCustomer.getPostalCode());
            ps.setString(8, newCustomer.getCountry());

            int noInserted = ps.executeUpdate();
            System.out.println((noInserted > 0 ? noInserted : "None ") + "inserted");
        } catch (SQLException exception) {
            System.err.println("There was a problem with SQL: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    //Get all Customers from DataBase
    public static void selectCustomers(Connection connection) throws SQLException {
        Statement s = connection.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + Customer.tableName);

        StringBuilder sb = new StringBuilder();
        while (rs.next()) {
            int id = rs.getInt("id");
            String username = rs.getString("username");
            String lastName = rs.getString("last_name");
            String firstName = rs.getString("first_name");
            String phone = rs.getString("phone");
            String address = rs.getString("address");
            String city = rs.getString("city");
            String postalCode = rs.getString("postal_code");
            String country = rs.getString("country");
            sb.append(
                    id + " | " +
                            username + " | " +
                            lastName + " |" +
                            firstName + " | " +
                            phone + " | " +
                            address + " | " +
                            city + " | " +
                            postalCode + " | " +
                            country + "\n");

        }
        System.out.print(sb);
    }

    //Delete Customer from DataBase

    public static boolean deleteCustomer(Connection connection, int id) throws SQLException {
        Statement s = connection.createStatement();
        int noDeleted = s.executeUpdate("DELETE FROM " + Customer.tableName + " WHERE `id` = " + id);
        return noDeleted > 0 ? true : false;
    }
}
