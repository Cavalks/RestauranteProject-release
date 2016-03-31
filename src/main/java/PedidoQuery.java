import java.math.BigDecimal;
import java.util.Date;

public class PedidoQuery {
	private Date date;
	private BigDecimal valor;

	public PedidoQuery(Date date, BigDecimal valor) {
		this.date = date;
		this.valor = valor;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "PedidoQuery [date=" + date + ", valor=" + valor + "]";
	}
	

}
