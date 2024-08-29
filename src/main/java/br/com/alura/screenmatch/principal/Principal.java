package br.com.alura.screenmatch.principal;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import io.github.cdimascio.dotenv.Dotenv;

public class Principal {

    Dotenv dotenv = Dotenv.load();

    private final ConsumoApi consumoApi = new ConsumoApi();
    private final ConverteDados conversor = new ConverteDados();
    private final Scanner scan = new Scanner(System.in);
    private static final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=" + dotenv.get("APIkey");
    private static final String SEASONS_PARAM = "&season=";

    public void displayMenu() {
        System.out.println("Digite o nome da serie que deseja buscar: ");
        var nomeSerie = scan.nextLine();

        var enderecoAPI = ENDERECO + nomeSerie.replace(" ", "+") + API_KEY;

        var json = consumoApi.obterDados(enderecoAPI);

        DadosSerie serie = conversor.obterDados(json, DadosSerie.class);

        clearScreen();

        System.out.println("========= Serie: " + serie.titulo() + " =========\nNumero de Temporadas: " + serie.totalTemporadas() + "\n"
                + "Score: " + serie.avaliacao() + " \n==================\n\n\n");

        displaySeasons(serie);

    }

    public void displaySeasons(DadosSerie serie) {

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= serie.totalTemporadas(); i++) {
            DadosTemporada temporada = conversor.obterDados(
                    consumoApi
                            .obterDados(ENDERECO + serie.titulo().replace(" ", "+") + API_KEY + SEASONS_PARAM + i),
                    DadosTemporada.class);

            temporadas.add(temporada);

        }

        temporadas.forEach(season -> {
            System.out.println("================= Season: " + season.numero() + " =================");
            season.episodios().forEach(episodio
                    -> System.out.println(episodio.numeroEpisodio() + "- Episode Title: " + episodio.titulo())
            );
            System.out.println("======================================");
        });
    }

    private void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

}
