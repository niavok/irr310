package fr.def.iss.vd2.lib_v3d.gui;

public class LabelStyle {

    private final String font;
    private final String style;
    private final int size;

    public LabelStyle(String font, String style, int size) {
        this.font = font;
        this.style = style;
        this.size = size;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((font == null) ? 0 : font.hashCode());
        result = prime * result + size;
        result = prime * result + ((style == null) ? 0 : style.hashCode());
        return result;
    }

    public String getFont() {
        return font;
    }

    public int getSize() {
        return size;
    }

    public String getStyle() {
        return style;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LabelStyle other = (LabelStyle) obj;
        if (font == null) {
            if (other.font != null)
                return false;
        } else if (!font.equals(other.font))
            return false;
        if (size != other.size)
            return false;
        if (style == null) {
            if (other.style != null)
                return false;
        } else if (!style.equals(other.style))
            return false;
        return true;
    }

}