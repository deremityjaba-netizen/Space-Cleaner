package ru.innovationcampus.vsu25.iudin_d_i.spacecleaner.view;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextView extends View {
    private BitmapFont font;
    private String text;

    public TextView(BitmapFont font, float x, float y){
        super(x, y);
        this.font = font;
    }

    public TextView(BitmapFont font, float x, float y, String text) {
        this(font, x, y);
        this.text = text;

        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        width = glyphLayout.width;
        height = glyphLayout.height;
    }
    public void setText(String text){
        this.text = text;
        GlyphLayout glyphLayout = new GlyphLayout(font,text);
        width = glyphLayout.width;
        height = glyphLayout.height;


    }

    @Override
    public void draw(SpriteBatch batch) {
        font.draw(batch, text, x, y);
    }
    public void dispose(){
        font.dispose();
    }
}
