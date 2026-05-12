package com.sistemahotel.demo;

import java.util.Scanner;

import com.sistemahotel.demo.dto.HospedeDTO;
import com.sistemahotel.demo.service.HospedeService;

public class Main {

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

                    break;

                case 2:

                    service.listarHospedes();

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
