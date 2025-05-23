package com.example.bike.utils;

import java.util.regex.Pattern;

/**
 * Classe para validar e formatar dados de entrada do usuário
 * Inclui validações para email, senha e telefone
 */

public class Validador {

    // Validar email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    // Validar senha - pelo menos 6 caracteres, uma maiúscula e um número
    private static final Pattern SENHA_PATTERN = Pattern.compile(
            "^(?=.*[A-Z])(?=.*\\d).{6,}$"
    );

    // Telefone BR no formato (XX)XXXXX-XXXX
    private static final Pattern TELEFONE_PATTERN = Pattern.compile(
            "^\\([0-9]{2}\\)[0-9]{5}-[0-9]{4}$"
    );

    /**
     * Valida o formato do email
     * @param email Email a ser validado
     * @return true se o email é válido, false caso contrário
     */
    public static boolean isEmailValido(String email) {
        return email != null && !email.trim().isEmpty() && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Valida o formato da senha
     * Deve ter pelo menos 6 caracteres, uma letra maiúscula e um número
     * @param senha Senha a ser validada
     * @return true se a senha é válida, false caso contrário
     */
    public static boolean isSenhaValida(String senha) {
        return senha != null && SENHA_PATTERN.matcher(senha).matches();
    }

    /**
     * Valida o formato do telefone brasileiro
     * Formato: (XX)XXXXX-XXXX
     * @param telefone Telefone a ser validado
     * @return true se o telefone é válido, false caso contrário
     */
    public static boolean isTelefoneValido(String telefone) {
        return telefone != null && TELEFONE_PATTERN.matcher(telefone).matches();
    }

    // ============== MÉTODOS PARA FORMATAÇÃO ==============

    /**
     * Aplica máscara de telefone enquanto o usuário digita
     * @param telefone Texto atual do campo
     * @return Texto formatado com máscara (XX)XXXXX-XXXX
     */
    public static String aplicarMascaraTelefone(String telefone) {
        if (telefone == null) return "";

        // Remove todos os caracteres não numéricos
        String numeros = telefone.replaceAll("[^0-9]", "");

        // Aplica a máscara baseada no tamanho
        if (numeros.length() == 0) {
            return "";
        } else if (numeros.length() <= 2) {
            return "(" + numeros;
        } else if (numeros.length() <= 7) {
            return "(" + numeros.substring(0, 2) + ")" + numeros.substring(2);
        } else if (numeros.length() <= 11) {
            return "(" + numeros.substring(0, 2) + ")" +
                    numeros.substring(2, 7) + "-" + numeros.substring(7);
        } else {
            // Limita a 11 dígitos
            return "(" + numeros.substring(0, 2) + ")" +
                    numeros.substring(2, 7) + "-" + numeros.substring(7, 11);
        }
    }

    /**
     * Remove a formatação do telefone, deixando apenas números
     * @param telefone Telefone formatado
     * @return String com apenas números
     */
    public static String limparTelefone(String telefone) {
        return telefone == null ? "" : telefone.replaceAll("[^0-9]", "");
    }

    /**
     * Gera o hash de uma senha usando PasswordUtils
     * @param senha Senha a ser hasheada
     * @return Hash da senha
     */
    public static String hashSenha(String senha) {
        return PasswordUtils.generateSecurePassword(senha);
    }

    /**
     * Verifica se a senha fornecida corresponde ao hash armazenado
     * @param senha Senha fornecida
     * @param senhaArmazenada Hash da senha armazenada
     * @return true se a senha é válida, false caso contrário
     */
    public static boolean verificarSenha(String senha, String senhaArmazenada) {
        return PasswordUtils.verifyPassword(senha, senhaArmazenada);
    }
}