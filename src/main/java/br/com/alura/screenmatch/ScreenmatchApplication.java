package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import io.github.cdimascio.dotenv.Dotenv;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// Load .env Variables
		Dotenv dotenv = Dotenv.load();
		// System.out.println(String.format("APIKey IS: %s %n%n%n",
		// dotenv.get("APIkey")));

		var consumoApi = new ConsumoApi();
		var json = consumoApi.obterDados("https://www.omdbapi.com/?t=supernatural&apikey=" + dotenv.get("APIkey"));
		// System.out.println(json);
		// json = consumoApi.obterDados("https://coffee.alexflipnote.dev/random.json");
		// System.out.println(json);
		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(String.format("%n%n%s%n%n", dados));

		DadosEpisodio episodio = conversor.obterDados(
				consumoApi
						.obterDados("https://www.omdbapi.com/?t=supernatural&season=1&episode=1&apikey=" + dotenv.get("APIkey")),
				DadosEpisodio.class);

		System.out.println(String.format("%n%n%s%n%n", episodio));

	}
}