import java.sql.*;
import java.util.ArrayList;

public class Order {
    private static String tableName = "orders";
    private static String orderDetailsTable = "orderdetails";
    private static String productsTable = "products";
    private int id;
    private Date order_date;
    private Date shipped_date;
    private String status;
    private String comments;
    private int customer_id;
    private int orderdetails_id;
    private String product_code;
    private int quantity;
    private double price;

    public Order(Date order_date, Date shipped_date, String status, String comments, int customer_id, String product_code, int quantity, double price) {
        this.order_date = order_date;
        this.shipped_date = shipped_date;
        this.status = status;
        this.comments = comments;
        this.customer_id = customer_id;
        this.product_code = product_code;
        this.quantity = quantity;
        this.price = price;
    }
    //******************************************************************************************************************
    //GENERATED GETTERS AND SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getOrder_date() {
        return order_date;
    }

    public void setOrder_date(Date order_date) {
        this.order_date = order_date;
    }

    public Date getShipped_date() {
        return shipped_date;
    }

    public void setShipped_date(Date shipped_date) {
        this.shipped_date = shipped_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getOrderdetails_id() {
        return orderdetails_id;
    }

    public void setOrderdetails_id(int orderdetails_id) {
        this.orderdetails_id = orderdetails_id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", order_date=" + order_date +
                ", shipped_date=" + shipped_date +
                ", status='" + status + '\'' +
                ", comments='" + comments + '\'' +
                ", customer_id=" + customer_id +
                ", orderdetails_id=" + orderdetails_id +
                '}';
    }

    //******************************************************************************************************************

    //Update the stock for a product after placing an order (inside use)
    public static void updateStock(Connection connection, String product_code) throws SQLException {
        String updateQuery = "UPDATE " + Order.productsTable + " SET stock = ? WHERE product_code = " + product_code;
        PreparedStatement ps = connection.prepareStatement(updateQuery);
        Statement s = connection.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + Order.productsTable + " WHERE product_code = " + product_code);
        rs.next();
        int newStock = rs.getInt("stock");
        if (newStock > 0) {
            newStock--;
            ps.setInt(1, newStock);
            ps.executeUpdate();
        }

    }

    //Add new order in DataBase

    public static boolean addOrder(Connection connection, Customer customer, Order order) throws SQLException {
        if (customer == null) return false;
        PreparedStatement ps = connection.prepareStatement("INSERT INTO " + Order.tableName +
                " (order_date, shipped_date, status, customer_id, orderdetails_id) " +
                "VALUES (DATE ? , DATE ?, ?, ?, ?)");
        PreparedStatement ps2 = connection.prepareStatement("INSERT INTO " + Order.orderDetailsTable +
                "(product_code, quantity, price_each) " +
                "VALUES(?, ?, ?)");

        ps2.setString(1, order.getProduct_code());
        ps2.setInt(2, order.getQuantity());
        ps2.setDouble(3, order.getPrice());
        int noInserted = ps2.executeUpdate();

        Statement s = connection.createStatement();
        ResultSet rs = s.executeQuery("SELECT MAX(id) as max_id FROM " + Order.orderDetailsTable);
        ps.setDate(1, order.getOrder_date());
        ps.setDate(2, order.getShipped_date());
        ps.setString(3, order.getStatus());
        ps.setInt(4, customer.getId());
        rs.next();
        ps.setInt(5, rs.getInt("max_id"));
        int noInserted2 = ps.executeUpdate();

        updateStock(connection, order.getProduct_code());

        return noInserted > 0;
    }

    //View order from DataBase

    public static ArrayList<Order> viewOrders(Connection connection, Customer customer) throws SQLException {
        ArrayList<Order> orders = new ArrayList<>();
        Statement s = connection.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM " + Order.tableName + " WHERE customer_id = " + customer.getId());

        while (rs.next()) {
            int id = rs.getInt("id");
            Date orderDate = rs.getDate("order_date");
            Date shippedDate = rs.getDate("shipped_date");
            String status = rs.getString("status");
            String commments = rs.getString("comments");
            int customerId = rs.getInt("customer_id");
            int orderDetailsId = rs.getInt("orderdetails_id");
            Statement s2 = connection.createStatement();
            ResultSet rs2 = s2.executeQuery("SELECT * FROM " + Order.orderDetailsTable + " WHERE id = " + orderDetailsId);
            rs2.next();
            String productCode = rs2.getString("product_code");
            int quantity = rs2.getInt("quantity");
            double price = rs2.getDouble("price_each");

            Order viewedOrder = new Order(
                    orderDate,
                    shippedDate,
                    status,
                    commments,
                    customerId,
                    productCode,
                    quantity,
                    price
            );
            viewedOrder.setOrderdetails_id(orderDetailsId);
            orders.add(viewedOrder);
        }

        return orders;
    }

    //Update the status of an order in DataBase

    public static void updateStatus(Connection connection, int id, String newStatus) throws SQLException {
        String updateQuery = "UPDATE " + Order.tableName + " SET status = ? WHERE id = " + id;
        PreparedStatement ps = connection.prepareStatement(updateQuery);
        ps.setString(1, newStatus);
        ps.executeUpdate();
    }

    //Add comment for an order in DataBase

    public static void addComment(Connection connection, int id, String comment) throws SQLException {
        String updateQuery = "UPDATE " + Order.tableName + " SET comments = ? WHERE id = " + id;
        PreparedStatement ps = connection.prepareStatement(updateQuery);
        ps.setString(1, comment);
        ps.executeUpdate();
    }

}
