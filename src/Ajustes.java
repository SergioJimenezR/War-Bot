
/*
 * @author Sergio Jimenez Roncero
 */

public interface Ajustes {

	public boolean publicar = true;

	// Incluir tantos participantes como se deseen.
	public String[] participantes = { "Persona 1", "Persona 2", "Persona 3", "Persona 4" };

	/*
	 * NOTA: Si el tweet con la lista de participantes incluida excede del limite de
	 * caracteres permitido en el tweet, poner a false.
	 */
	public boolean publicarListaDeParticipantes = true;

	public boolean SuicidiosActivados = false;

	public int MesDeComienzo = 1; // Intervalo [1, 12]
	public int AnioDeComienzo = 2020; // AñoDeComienzo >= 0

	/*
	 * NOTA: Compilar y ejecutar el codigo desde un ordenador permanente (p.ej.
	 * Raspberry Pi) al momento del primer tweet.
	 */
	public int repeticionesDiarias = 12;
	public int PAUSA_DIURNA = 0; // 1 h = 3600000 ms
	public int PAUSA_NOCTURNA = 0; // 12 h = 43200000 ms

	// Reclamar Keys y Tokens a Twitter Developers.
	public String CONSUMER_KEY = "";
	public String CONSUMER_SECRET = "";
	public String ACCESS_TOKEN = "";
	public String ACCESS_SECRET = "";

}