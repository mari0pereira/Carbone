package com.example.bike.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * TextWatcher personalizado para aplicar máscaras em tempo real
 * Suporta máscaras de telefone e CPF
 */
public class Mascara implements TextWatcher {

    public enum TipoMascara {
        TELEFONE,
    }

    private final EditText editText;
    private final TipoMascara tipoMascara;
    private boolean isUpdating = false;

    /**
     * Construtor para criar o TextWatcher com máscara
     * @param editText Campo de texto que receberá a máscara
     * @param tipoMascara Tipo de máscara a ser aplicada
     */
    public Mascara(EditText editText, TipoMascara tipoMascara) {
        this.editText = editText;
        this.tipoMascara = tipoMascara;
    }

    @Override // Esses @Overrides foram criados automaticamente com a criação da classe e implements 'TextWatcher'
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Não precisa fazer nada antes da mudança
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Não precisa fazer nada durante a mudança
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // Evita loop infinito
        if (isUpdating) {
            return;
        }

        isUpdating = true;

        String textoAtual = editable.toString();
        String textoFormatado;

        // Aplica a máscara baseada no tipo -- talvez o cPF seja aplicado
        switch (tipoMascara) {
            case TELEFONE:
                textoFormatado = Validador.aplicarMascaraTelefone(textoAtual);
                break;
            default:
                textoFormatado = textoAtual;
                break;
        }

        // Atualiza o campo apenas se o texto mudou
        if (!textoFormatado.equals(textoAtual)) {
            // Salva a posição do cursor
            int cursorPosition = editText.getSelectionStart();

            // Atualiza o texto
            editText.setText(textoFormatado);

            // Reposiciona o cursor
            int novaPosicao = Math.min(cursorPosition + (textoFormatado.length() - textoAtual.length()),
                    textoFormatado.length());
            if (novaPosicao >= 0) {
                editText.setSelection(novaPosicao);
            }
        }

        isUpdating = false;
    }

    /**
     * Método para facilitar a aplicação da máscara de telefone
     * @param editText Campo de texto
     */
    public static void aplicarMascaraTelefone(EditText editText) {
        editText.addTextChangedListener(new Mascara(editText, TipoMascara.TELEFONE));
    }
}