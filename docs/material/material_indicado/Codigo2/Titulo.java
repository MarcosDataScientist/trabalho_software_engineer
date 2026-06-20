package src;

public class Titulo {
int prazo;
public Titulo(int codigo)
{
	this.prazo = codigo+1;
}
public int getPrazo() {
	return prazo;
}

public void setPrazo(int prazo) {
	this.prazo = prazo;
}

}