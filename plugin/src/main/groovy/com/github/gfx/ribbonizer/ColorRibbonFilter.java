package com.github.gfx.ribbonizer;

import com.android.build.gradle.api.ApplicationVariant;

import org.gradle.api.Action;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class ColorRibbonFilter implements Action<BufferedImage> {
    boolean debug = false;

    final Color ribbonColor;
    final Color labelColor;

    String label;
    String fontName = "Default";
    int fontStyle = Font.PLAIN;

    public ColorRibbonFilter(String label, Color ribbonColor, Color labelColor) {
        this.label = label;
        this.ribbonColor = ribbonColor;
        this.labelColor = labelColor;
    }

    public ColorRibbonFilter(ApplicationVariant variant, Color ribbonColor, Color labelColor) {
        this(variant.getBuildType().getName(), ribbonColor, labelColor);
    }

    public ColorRibbonFilter(ApplicationVariant variant, Color ribbonColor) {
        this(variant, ribbonColor, Color.WHITE);
    }


    @Override
    public void execute(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        Graphics2D g = (Graphics2D)image.getGraphics();

        g.setTransform(AffineTransform.getRotateInstance(Math.toRadians(-45)));

        int y = height / 4;

        // ribbon
        g.setColor(ribbonColor);
        g.fillRect(-width, y, width * 2, height / 5);

        // label
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(labelColor);

        int maxLabelWidth = calculateMaxLabelWidth(y);
        g.setFont(getFont(maxLabelWidth, g.getTransform()));
        Rectangle2D labelBounds = g.getFont().getStringBounds(label,
                new FontRenderContext(g.getTransform(), true, true));

        g.drawString(label, (int)(-labelBounds.getWidth() / 2), (int)(y + labelBounds.getHeight()));

        if (debug) {
            g.drawRect((int)(-labelBounds.getWidth() / 2), y, maxLabelWidth, (int)labelBounds.getHeight());
        }

        g.dispose();
    }

    static int calculateMaxLabelWidth(int y) {
        return (int)Math.sqrt(Math.pow(y, 2) * 2);
    }

    Font getFont(int maxLabelWidth, AffineTransform transform) {
        int max = 32;
        int min = 0;
        int x = max;
        FontRenderContext frc = new FontRenderContext(transform, true, true);

        for (int i = 0; i < 10; i++) {
            int m = ((max + min) / 2);
            if (m == x) {
                break;
            }

            Font font = new Font(fontName, fontStyle, m);
            Rectangle2D labelBounds = font.getStringBounds(label, frc);
            int px = (int)labelBounds.getWidth();

            if (px > maxLabelWidth) {
                max = m;
            } else {
                min = m;
            }
            x = m;
        }
        return new Font(fontName, fontStyle, x);
    }
}