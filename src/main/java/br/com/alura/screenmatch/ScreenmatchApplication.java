package br.com.alura.screenmatch;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import io.github.cdimascio.dotenv.Dotenv;

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

        System.out.println("\n\n" + dados + "\n\n");

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dados.totalTemporadas(); i++) {
            DadosTemporada temporada = conversor.obterDados(
                    consumoApi
                            .obterDados("https://www.omdbapi.com/?t=supernatural&season=" + i + "&apikey=" + dotenv.get("APIkey")),
                    DadosTemporada.class);

            temporadas.add(temporada);

        }

        temporadas.forEach(season -> {
            System.out.println("\n" + season);
        });

    }
}
