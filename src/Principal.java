
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/* 
 * @author Sergio Jimenez Roncero
 * Implementado con Twitter4J
 * Verano 2019 - Tiempo dedicado: 3 horas 
 */

public class Principal implements Ajustes {

	static Persona[] vpersonas = new Persona[participantes.length];
	static int nPersonas = 0;
	static int nMuertos = 0;
	static int nSuicidios = 0;

	public static void main(String[] args) {

		// Preparacion del vector
		for (int i = 0; i < participantes.length; i++)
			if (nPersonas < vpersonas.length)
				vpersonas[nPersonas++] = new Persona(participantes[i]);

		String tweet = "La guerra esta a punto de empezar!\nNumero de participantes: " + participantes.length + ".\n";
		if (publicarListaDeParticipantes)
			tweet += "Participantes: " + listadoVivos() + "\n";
		tweet += "#HueleASangre";
		tweetear(tweet);

		while (participantes.length - nMuertos > 1) {
			for (int i = 0; i < repeticionesDiarias && participantes.length-nMuertos > 1; i++) {
				tweet = obtenerFecha() + matar(vpersonas[encontrarVivo()], vpersonas[encontrarVivo()]);
				if (participantes.length - nMuertos != 1) { // Normalidad
					tweet += participantes.length - nMuertos + " personas vivas.\n";
					if (publicarListaDeParticipantes)
						tweet += "Vivos: " + listadoVivos() + "\n";
				} else // Ultima persona / tweet
					tweet += "Ganador: " + listadoVivos() + "\n";
				tweet += "#HueleASangre";

				tweetear(tweet);

				pausar(PAUSA_DIURNA);
			}
			pausar(PAUSA_NOCTURNA);
		}

		if (SuicidiosActivados)
			if (nSuicidios == 0)
				tweetear("No se ha producido ningun suicidio.");
			else if (nSuicidios == 1)
				tweetear("Se ha producido 1 suicidio.");
			else
				tweetear("Se han producido " + nSuicidios + " suicidios.");

		tweetear("Programa desarrollado en su totalidad por Sergio Jimenez.");

	}

	/*
	 * vectorVivos() guarda las posiciones vivas del vector "vpersonas" en las
	 * posiciones de un vector "vectorVivos". Devuelve dicho vector.
	 */
	private static int[] vectorVivos() {
		int[] vivos = new int[vpersonas.length - nMuertos];
		for (int i = 0, pVivo = 0; i < vpersonas.length && pVivo < vivos.length; i++)
			if (vpersonas[i].getVivo())
				vivos[pVivo++] = i;
		return vivos;
	}

	// Devuelve la posicion de una persona viva cualquiera del vector "vpersonas".
	private static int encontrarVivo() {
		return vectorVivos()[(int) (Math.random() * (vectorVivos().length))];
	}

	/*
	 * De 2 personas vivas sacadas aleatoriamente, contempla si los suicidios estan
	 * activados, o sino se busca otra persona en el caso de que sean iguales, y
	 * procede a matar a la persona elegida para morir.
	 */
	private static String matar(Persona pviva, Persona pmuerta) {
		if (SuicidiosActivados) {
			if (pviva == pmuerta)
				nSuicidios++;
		} else // Buscar otra persona
			while (pviva == pmuerta)
				pmuerta = vpersonas[encontrarVivo()];

		pmuerta.setVivo(false);
		nMuertos++;
		if (pviva.getNombre() == pmuerta.getNombre()) // Suicidio
			return pviva.getNombre() + " vio la situacion demasiado cruda y decidio suicidarse.\n";
		else // Asesinato
			return pviva.getNombre() + " ha matado a " + pmuerta.getNombre() + ".\n";
	}

	// Devuelve una cadena de texto con el listado de personas vivas
	private static String listadoVivos() {
		String lista = "";
		for (int i = 0; i < vectorVivos().length; i++)
			if (i < vectorVivos().length - 2)
				lista += vpersonas[vectorVivos()[i]].getNombre() + ", ";
			else if (i == vectorVivos().length - 2) // Penultima persona de la lista
				lista += vpersonas[vectorVivos()[i]].getNombre() + " y ";
			else // Ultima persona de la lista
				lista += vpersonas[vectorVivos()[i]].getNombre() + ".";

		return lista;
	}

	static int mes = MesDeComienzo;
	static int anio = AnioDeComienzo;

	private static String obtenerFecha() {
		String[] meses = { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre",
				"Octubre", "Noviembre", "Diciembre" };

		if (mes == 13) {
			mes = 1;
			anio++;
		}

		// Formula numero aleatorio: (int)(Math.random()*(maximo+1-minimo)+minimo)
		if (mes == 2) // Febrero
			if (anio % 4 == 0) // Anio bisiesto
				return (int) (Math.random() * 29 + 1) + " de " + meses[mes++ - 1] + " de " + anio + ":\n";
			else // Anio no bisiesto
				return (int) (Math.random() * 28 + 1) + " de " + meses[mes++ - 1] + " de " + anio + ":\n";
		else if (mes == 4 || mes == 6 || mes == 9 || mes == 11) // Meses de 30 dias
			return (int) (Math.random() * 30 + 1) + " de " + meses[mes++ - 1] + " de " + anio + ":\n";
		else // Meses de 31 dias
			return (int) (Math.random() * 31 + 1) + " de " + meses[mes++ - 1] + " de " + anio + ":\n";
	}

	static String tweets = "";

	private static void tweetear(String tweet) {
		try {
			// Creacion de copia de seguridad automatica y local a traves de variable tweets.
			PrintWriter pw = new PrintWriter(new File("Tweets.txt"));
			tweets += tweet + "\n\n";
			pw.println(tweets);
			pw.close(); // Guardado

			if (publicar) {
				Status status = new TwitterFactory(new ConfigurationBuilder().setDebugEnabled(true)
						.setOAuthConsumerKey(CONSUMER_KEY).setOAuthConsumerSecret(CONSUMER_SECRET)
						.setOAuthAccessToken(ACCESS_TOKEN).setOAuthAccessTokenSecret(ACCESS_SECRET).build())
								.getInstance().updateStatus(tweet); // Publicacion oficial del tweet
				System.out.println(status.getText() + "\n(" + status.getText().length() + " caracteres)\n");

			} else
				System.out.println(tweet + "\n");
		} catch (FileNotFoundException exc1) {
			System.out.println("Fichero no encontrado. FileNotFoundException.\n");
			exc1.printStackTrace();
		} catch (TwitterException exc2) {
			System.out.println("Error producido. TwitterException.\n" + exc2.getStackTrace());
			exc2.printStackTrace();
		}
	}

	private static void pausar(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException exc) {
			System.out.println("Error producido. InterruptedException.\n");
			exc.printStackTrace();
		}
	}

}