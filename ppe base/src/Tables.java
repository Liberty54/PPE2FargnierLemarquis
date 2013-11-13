import java.io.*;
import java.sql.*;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: gsancho
 * Date: 06/11/13
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */
public class Tables {


    private String resu =null;

    public void runMenu(){

        Connection co = this.getConnection();


    }
    public void affichageconnection () {


    }
    public String[] getNomsColonnes(ResultSet resultat) throws SQLException{
        ResultSetMetaData metadata = resultat.getMetaData();
        String[] noms = new String[metadata.getColumnCount()];
        for(int i = 0; i < noms.length; i++){
            String nomColonne = metadata.getColumnName(i+1);
            noms[i] = nomColonne;
        }
        return noms;
    }

    //On fait rentrer à l'utilisateur les données pour se connecter au serveur

    public Connection getConnection() {


        Scanner sc = new Scanner(System.in);
        System.out.println("Adresse :");
        String url = sc.nextLine();
        System.out.println("Utilisateur :");
        String user = sc.nextLine();
        System.out.println("Mot de passe :");
        String password = sc.nextLine();

        System.out.println("-------- MySQL JDBC Connection Testing ------------");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
            e.printStackTrace();

        }

        System.out.println("MySQL JDBC Driver Registered!");
        Connection connection = null;

        try {
            connection = DriverManager
                    .getConnection("jdbc:mysql://"+url, user, password);

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }

        if (connection != null) {
            System.out.println("You made it, take control your database now!");
        } else {
            System.out.println("Failed to make connection!");
        }

        //On récupére le nom des bases


        try {
            connection = DriverManager.getConnection("jdbc:mysql://"+url,user,password);
            //on récupère les métadonnées à partir de la Connection
            DatabaseMetaData dmd = connection.getMetaData();
            //récupération des informations
            String catalogTerm = dmd.getCatalogTerm();
            ResultSet resultat = dmd.getCatalogs();
            //affichage des informations
            System.out.println("Terme du SGBD pour catalogue = "+catalogTerm);
            while(resultat.next()){
                String nomCatalog = resultat.getString("TABLE_CAT");
                System.out.println(catalogTerm+" "+resultat.getRow()+ " = "+nomCatalog);
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }



        //On se connecte à la base de donnée qui est entrée par l'utilisateur

        System.out.println("Indiquez le nom de la base à laquelle vous voulez vous connecter :");
        String base = sc.nextLine();

        url = url + "/" + base;

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
            e.printStackTrace();

        }

        System.out.println("MySQL JDBC Driver Registered!");
        connection = null;

        try {
            connection = DriverManager
                    .getConnection("jdbc:mysql://"+url, user, password);

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }

        if (connection != null) {
            System.out.println("Connexion effectuée");
        } else {
            System.out.println("Failed to make connection!");
        }






//on récupère les métadonnées à partir de la connexion
        DatabaseMetaData dmd = null;
        try {
            dmd = connection.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
//récupération des informations
        ResultSet tables = null;
        try {
            tables = dmd.getTables(connection.getCatalog(),null,"%",null);
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
//affichage des informations
        try {
            System.out.println("Liste des tables de la base "+ base+" : ");
            while(tables.next()){
                for(int i=0; i<tables.getMetaData().getColumnCount();i++){
                    String nomColonne = tables.getMetaData().getColumnName(i+1);
                    Object valeurColonne = tables.getObject(i+1);
                    if (nomColonne.equals("TABLE_NAME")){
                        System.out.println("- "+valeurColonne);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


             //On affiche la table entrée par l'utilisateur

        System.out.println("Quelle table voulez vous afficher le formulaire ? ");
        String table = sc.nextLine();
        try {
            connection = DriverManager.getConnection("jdbc:mysql://"+url, user, password);
            String sql = "SELECT * FROM " +table;
            Statement statement = connection.createStatement();
            ResultSet resultat = statement.executeQuery(sql);
            String[] noms = getNomsColonnes(resultat);
            for(int i = 0; i < noms.length; i++){
                System.out.println(i + " " + noms[i]);
            }

            // On sélectionne la catégorie entrée par l'utilisateur

            System.out.println("Sélectionnez la catégorie: ");
            int num = sc.nextInt();
            System.out.println(noms[num]);


            //On demande à l'utilisateur s'il veut son formulaire en POST ou GET

            String deb="<FORM NAME=? ACTION=? METHOD=? >";
            String fin = "</FORM>";
            String action = null;
            System.out.println("Voulez vous votre formulaire en POST ou GET ?");
            action = sc.next();
            if (action.equals("POST")){

                 deb="<FORM NAME=? ACTION=POST METHOD=? >";
                 fin = "</FORM>";
            }
            if (action.equals("GET")){
                deb="<FORM NAME=? ACTION=GET METHOD=? >";
                fin = "</FORM>";
            }




            //On sélectionne le type de balise que l'utilisateur veut faire apparaitre à son formulaire

            String choix = null;
            System.out.println("Text, Password, Select ?");
            choix = sc.next();

            String Newligne=System.getProperty("line.separator");

            if(choix.equals("Text")){


                resu=(deb+""+Newligne+"<input type="+ choix +" value ='VALEURS A CHANGER' name='"+noms[num]+"'/>"+Newligne+""+fin);
                System.out.println(resu);
            }
            if(choix.equals("Select")){


                 resu =(deb+""+Newligne+"\t<SELECT name="+noms[num]+">"+Newligne+"" +
                        "\t\t<OPTION VALUE=\"VALEURS A CHANGER\">VALEURS A CHANGER</OPTION>\n" +
                        "\t\t<OPTION VALUE=\"VALEURS A CHANGER\">VALEURS A CHANGER</OPTION>\n" +
                        "\t\t<OPTION VALUE=\"VALEURS A CHANGER\">VALEURS A CHANGER</OPTION>\n" +
                        "\t\t<OPTION VALUE=\"VALEURS A CHANGER\">VALEURS A CHANGER</OPTION>\n" +
                        "\t\t<OPTION VALUE=\"VALEURS A CHANGER\">VALEURS A CHANGER</OPTION>\n" +
                        "\t</SELECT>"+Newligne+""+fin);

                System.out.println(resu);

            }
            if (choix.equals("Password")){
                resu=(deb+"<INPUT TYPE=\"password\" NAME="+noms[num]+" SIZE=\"8\" MAXLENGTH=\"8\">"+fin);
                System.out.println(resu);
            }

            //On crée le fichier .txt à la racine du projet

            try{
                BufferedWriter writer = new BufferedWriter(new FileWriter(new File(noms[num]+".txt")));
//si le fichier n'existe pas, il est crée à la racine du projet
                writer.write(resu);

                writer.close();

            }

            catch (IOException e){
                e.printStackTrace();
            }


        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }



        return connection;
    }
}
