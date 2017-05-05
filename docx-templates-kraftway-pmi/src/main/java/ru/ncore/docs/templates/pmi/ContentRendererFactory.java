package ru.ncore.docs.templates.pmi;

import ru.ncore.docs.docbook.document.ChapterContent;


/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public class ContentRendererFactory {
    public static IContentRenderer getRenderer(ChapterContent contentData) {
        switch (contentData.getType()) {
            case PARA:
                return (new ParaRenderer()).setContent(contentData);
            case PROGRAMLISTING:
                return (new ListingRenderer()).setContent(contentData);
            case SECTION:
                return (new SectionRenderer()).setContent(contentData);
            case ORDEREDLIST:
            case ITEMLIST: {
                IContentRenderer ilistRendere = new ItemizedListRenderer();
                ilistRendere.setContent(contentData);
                return ilistRendere;
            }
            default:
                System.out.printf("[W002] unknown renderer %s\n", contentData.getType().toString());
                System.out.printf("[D003] %s\n", contentData.getTitle());
                return null;
        }
    }
}
