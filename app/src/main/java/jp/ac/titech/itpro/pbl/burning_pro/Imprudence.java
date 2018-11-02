package jp.ac.titech.itpro.pbl.burning_pro;

import java.util.*;

public class Imprudence {
    public String generateImprudenceText() {
        List<String> imprudence_texts = Arrays.asList(
            "授業に一回くらい出なくたって俺らには大した問題じゃない",
            "大学生なんだから自分で判断して休みたかったら休めばいいのではと思ってしまいます",
            "何だろう",
            "みんなで考えよう"
        );

        Collections.shuffle(imprudence_texts);
        String imprudence_text = imprudence_texts.get(0);

        return imprudence_text;
    }
}
