import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Session;

import com.joaofnunes.model.Funcionario;
import com.joaofnunes.model.Meta;
import com.joaofnunes.model.Status;

public class Main {
	public static void main(String[] args) {

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("PedidoPU");
		EntityManager manager = factory.createEntityManager();
		Session session = (Session) manager.unwrap(Session.class);
		EntityTransaction trx = manager.getTransaction();
		trx.begin();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 2);
		Meta meta = manager.find(Meta.class, new Long(1));
		meta.setStatusMeta(Status.EM_ANDAMENTO);
		meta.setValorMeta(new BigDecimal(900));
		meta.setValorAndamentoMeta(new BigDecimal(0));
		meta.setDataFinal(calendar.getTime());
		// manager.merge(meta);
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTime(meta.getDataFinal());
		// calendar.add(Calendar.ye, -5);
		// meta.setValorAndamentoMeta(new BigDecimal(0));
		// meta.setValorMeta(new BigDecimal(400));
		// meta.setDataFinal(calendar.getTime());
		trx.commit();
		SimpleDateFormat simple = new SimpleDateFormat("dd/MM/yyyy");
		System.out.println("inicial " + simple.format(meta.getDataInicial()));
		System.out.println("final " + simple.format(meta.getDataFinal()));
		System.out.println("hoje " + simple.format(new Date()));

		session.close();
	}

	public static List<Object[]> lista(EntityManager manager) {
		Calendar dataInicial = Calendar.getInstance();
		dataInicial.add(Calendar.DAY_OF_MONTH, -7);
		Calendar dataFinal = Calendar.getInstance();
		@SuppressWarnings("unchecked")
		List<Object[]> lista = manager
				.createQuery(
						"Select DATE(c.dataCriacao),sum(c.valorTotal) from Pedido c  where c.dataCriacao between :dateBegin AND :dateEnd group by DATE(c.dataCriacao) order by ")
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
