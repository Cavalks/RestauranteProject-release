import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.time.DateUtils;

public class Main {
	public static void main(String[] args) {

		// EntityManagerFactory factory =
		// Persistence.createEntityManagerFactory("PedidoPU");
		// EntityManager manager = factory.createEntityManager();
		// Session session = manager.unwrap(Session.class);
		// List<Object[]> lista =
		// manager.createQuery("Select MONTH(P.dataCriacao) , sum(P.valorTotal)
		// from Pedido P group by MONTH(P.dataCriacao) ").getResultList();
		// for (Object[] objects : lista) {
		// System.out.println(objects[0]);
		// System.out.println(objects[1]);
		// }
		// session.close();
	}

	public static List<Object[]> lista(EntityManager manager) {
		Calendar dataInicial = Calendar.getInstance();
		dataInicial.add(Calendar.DAY_OF_MONTH, -7);
		Calendar dataFinal = Calendar.getInstance();
		@SuppressWarnings("unchecked")
		List<Object[]> lista = manager
				.createQuery(
						"Select DATE(c.dataCriacao),sum(c.valorTotal) from Pedido c  where c.dataCriacao between :dateBegin AND :dateEnd group by DATE(c.dataCriacao)")
				.setParameter("dateBegin", dataInicial.getTime()).setParameter("dateEnd", dataFinal.getTime())
				.getResultList();
		;
		return lista;
	}

	public static List<PedidoQuery> preencherLista7() {
		Calendar calendar = Calendar.getInstance();
		List<PedidoQuery> pedidos = new ArrayList<>();
		calendar = DateUtils.truncate(calendar, Calendar.DAY_OF_MONTH);
		PedidoQuery pedidoQuery = null;
		for (int i = 0; i < 7; i++) {
			pedidoQuery = new PedidoQuery(calendar.getTime(), new BigDecimal(0));
			pedidos.add(pedidoQuery);
			calendar.add(Calendar.DAY_OF_MONTH, -1);
		}

		return pedidos;

	}

	public static List<PedidoQuery> operarListas(List<Object[]> lista1, List<PedidoQuery> lista2) {

		for (Object[] pedido : lista1) {

			for (PedidoQuery pedido2 : lista2) {
				Date date = (Date) pedido[0];
				if (date.equals(pedido2.getDate())) {
					BigDecimal valor = (BigDecimal) pedido[1];
					pedido2.setValor(valor);
				}

			}

		}

		return lista2;
	}

}
