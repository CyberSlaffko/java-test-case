package ru.ncore.docs.docbook;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public class DocumentNotFound extends Document {
    public DocumentNotFound() {
        super("");
    }

    public boolean isParsedSuccessfully() {
        return false;
    }

    public Document.Type getType() {
        return Type.ERROR;
    }
}
