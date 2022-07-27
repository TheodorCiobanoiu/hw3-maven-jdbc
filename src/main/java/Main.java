import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {

    private static Connection connect() throws SQLException {
        String connectionUrl = "jdbc:mysql://localhost:3306/dbsummer_tema3_tcv";
        String username = "root";
        String password = "root";
        return DriverManager.getConnection(connectionUrl, username, password);
    }

    public static void main(String[] args) {
        try {
            Connection connection = Main.connect();
            Customer.selectCustomers(connection);
            Customer customer = new Customer(
                    1,
                    "andrew",
                    "Ginescu",
                    "Andrei",
                    "0799988764",
                    "Bulevardul Trasurilor 92",
                    "Baia Mare",
                    "13485",
                    "Romania"
            );
            Order order = new Order(
                    Date.valueOf("2022-07-27"),
                    Date.valueOf("2022-07-29"),
                    "In progress",
                    "-",
                    7,
                    "SALCF",
                    1,
                    3999.99
            );

//            Linii de cod comentate pentru testare. Rularea lor de fiecare data cand s-a dorit testarea functionalitatii ar fi dus la erori sau executii nedorite

//            Customer.insertCustomer(connection,customer);
//            System.out.println(Customer.getById(connection,13));
//            Customer customer = Customer.getById(connection, 3);
//            System.out.println(customer);
//            System.out.println(Customer.delete(connection, 9) ? "s-a sters" : "nu s-a sters");
//            boolean added = Order.addOrder(connection, customer, new Order("2022-01-01", "2022-01-01", "in progress", customer.id));
//            System.out.println((added ? "added " : " not added") + " order");
//            boolean deleted = Customer.deleteCustomer(connection, 13);
//            int deletedId = 13;
//            System.out.println((deleted ? "Deleted customer with ID" : "Could not delete customer with ID " + deletedId));
//            Order.addOrder(connection, Customer.getById(connection, 7), order);


            ArrayList<Order> orders = new ArrayList<>();
            orders = Order.viewOrders(connection, Customer.getById(connection, 2));
            Order.updateStatus(connection, 9, "Done!");
            Order.addComment(connection, 7, "comentariu");
            for (Order itOrder : orders) {
                System.out.println(itOrder);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

}
