package com.sistemahotel.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.sistemahotel.demo.dto.HospedeDTO;
import com.sistemahotel.demo.service.HospedeService;

public class Main {

    // Listas em memória (sem Spring/JPA)
    static List<String> quartosCadastrados = new ArrayList<>();
    static List<String> reservasCadastradas = new ArrayList<>();
    static long proximoIdReserva = 1;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        HospedeService service = new HospedeService();

        int opcao;

        do {

            System.out.println("\n===== HOTEL =====");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar");
            System.out.println("3 - Atualizar");
            System.out.println("4 - Remover");
            System.out.println("0 - Sair");

            System.out.print("Escolha: ");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {

                case 1:

                    // Submenu de cadastro
                    System.out.println("\n--- CADASTRAR ---");
                    System.out.println("1 - Hóspede");
                    System.out.println("2 - Quarto");
                    System.out.println("3 - Reserva");
                    System.out.println("0 - Voltar");
                    System.out.print("Escolha: ");

                    int tipoCadastro = sc.nextInt();
                    sc.nextLine();

                    if (tipoCadastro == 1) {

                        // Cadastrar Hóspede
                        System.out.println("\n-- Cadastro de Hóspede --");

                        System.out.print("Nome: ");
                        String nome = sc.nextLine();

                        System.out.print("CPF: ");
                        String cpf = sc.nextLine();

                        System.out.print("Telefone: ");
                        String telefone = sc.nextLine();

                        System.out.print("Email: ");
                        String email = sc.nextLine();

                        HospedeDTO dto = new HospedeDTO(nome, cpf, telefone, email);
                        service.cadastrarHospede(dto);

                    } else if (tipoCadastro == 2) {

                        // Cadastrar Quarto
                        System.out.println("\n-- Cadastro de Quarto --");

                        System.out.print("Número do quarto: ");
                        int numero = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Tipo (ex: Standard, Luxo, Suite): ");
                        String tipo = sc.nextLine();

                        System.out.print("Preço (ex: 150,00): ");
                        double preco = sc.nextDouble();
                        sc.nextLine();

                        String quarto = String.format("Quarto #%d | Tipo: %s | Preço: R$ %.2f", numero, tipo, preco);
                        quartosCadastrados.add(quarto);

                        System.out.println("\nQuarto cadastrado com sucesso!");

                    } else if (tipoCadastro == 3) {

                        // Cadastrar Reserva
                        System.out.println("\n-- Cadastro de Reserva --");

                        System.out.print("ID do Hóspede: ");
                        long hospedeId = sc.nextLong();
                        sc.nextLine();

                        System.out.print("Número do Quarto: ");
                        int quartoNumero = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Data de Entrada (dd/mm/aaaa): ");
                        String dataEntrada = sc.nextLine();

                        System.out.print("Data de Saída (dd/mm/aaaa): ");
                        String dataSaida = sc.nextLine();

                        String reserva = String.format(
                            "Reserva #%d | Hóspede ID: %d | Quarto: #%d | Entrada: %s | Saída: %s | Status: ATIVA",
                            proximoIdReserva++, hospedeId, quartoNumero, dataEntrada, dataSaida
                        );
                        reservasCadastradas.add(reserva);

                        System.out.println("\nReserva cadastrada com sucesso!");

                    } else if (tipoCadastro == 0) {
                        System.out.println("Voltando ao menu principal...");
                    } else {
                        System.out.println("Opção inválida.");
                    }

                    break;

                case 2:

                    service.listarHospedes();

                    if (!quartosCadastrados.isEmpty()) {
                        System.out.println("\n--- Quartos Cadastrados ---");
                        for (String q : quartosCadastrados) {
                            System.out.println(q);
                            System.out.println("----------------------");
                        }
                    }

                    if (!reservasCadastradas.isEmpty()) {
                        System.out.println("\n--- Reservas Cadastradas ---");
                        for (String r : reservasCadastradas) {
                            System.out.println(r);
                            System.out.println("----------------------");
                        }
                    }

                    break;

                case 3:

                    System.out.print("ID: ");
                    Long idAtualizar = sc.nextLong();
                    sc.nextLine();

                    System.out.print("Novo nome: ");
                    String novoNome = sc.nextLine();

                    System.out.print("Novo telefone: ");
                    String novoTelefone = sc.nextLine();

                    System.out.print("Novo email: ");
                    String novoEmail = sc.nextLine();

                    HospedeDTO atualizarDTO = new HospedeDTO(novoNome, "", novoTelefone, novoEmail);

                    service.atualizarHospede(idAtualizar, atualizarDTO);

                    break;

                case 4:

                    System.out.print("ID: ");
                    Long idRemover = sc.nextLong();
                    sc.nextLine();

                    service.removerHospede(idRemover);

                    break;

                case 0:

                    System.out.println("Sistema encerrado.");
                    break;

                default:

                    System.out.println("Opção inválida.");
            }

        } while (opcao != 0);

        sc.close();
    }
}
