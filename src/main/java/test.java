import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class test {

	public static void main(String[] args) {
		
	EntityManagerFactory factory = Persistence.createEntityManagerFactory("PedidoPU");
	EntityManager manager  = factory.createEntityManager();
	EntityTransaction transaction = manager.getTransaction();
	
	transaction.begin();
	
	
	
	
	transaction.commit();
	


	}

}
