package info.vividcode.android.app.example.cra;

public interface ListItem {

    Type getType();

    enum Type {
        SECTION_HEADER, CONTENT
    }

    class Content implements ListItem {
        public final String data;
        public Content(String data) {
            this.data = data;
        }
        @Override
        public Type getType() {
            return Type.CONTENT;
        }
    }

    class SectionHeader implements ListItem {
        public final String title;
        public SectionHeader(String title) {
            this.title = title;
        }
        @Override
        public Type getType() {
            return Type.SECTION_HEADER;
        }
    }

}
