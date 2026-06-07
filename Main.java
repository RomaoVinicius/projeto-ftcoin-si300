public class Main {
    public static void main(String[] args) {
        CarteiraDAO dao = new CarteiraDAOMemoria();
        
        CarteiraController controller = new CarteiraController(dao);
        
        CarteiraView view = new CarteiraView(controller);
        
        view.exibirMenu();
    }
}