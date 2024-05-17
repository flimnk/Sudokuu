package bahea.sudoku;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SudokuApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SudokuApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal  principal =new Principal();
		principal.jogo();
	}
}