package com.example.bike.utils;

import java.util.regex.Pattern;

 // Classe para validar as entradas de dados do usuário
public class Validador {

    // Validar email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
    );
    // Validar senha - mínimo 6 caracteres, pelo menos uma letra e um número
    private static final Pattern SENHA_PATTERN = Pattern.compile(
            "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$"
    );
    // Validar de telefone BR no formato (XX)XXXXX-XXXX
    private static final Pattern TELEFONE_PATTERN = Pattern.compile(
            "^\\(\\d{2}\\)\\d{5}-\\d{4}$"
    );

    /**
     * Valida o formato do email
     * @param email Email a ser validado
     * @return true se o email é válido, false caso contrário
     */
    public static boolean isEmailValido(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Valida o formato da senha
     * @param senha Senha a ser validada
     * @return true se a senha é válida, false caso contrário
     */
    public static boolean isSenhaValida(String senha) {
        return senha != null && SENHA_PATTERN.matcher(senha).matches();
    }

    /**
     * Valida o formato do telefone
     * @param telefone Telefone a ser validado
     * @return true se o telefone é válido, false caso contrário
     */
    public static boolean isTelefoneValido(String telefone) {
        return telefone != null && TELEFONE_PATTERN.matcher(telefone).matches();
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
