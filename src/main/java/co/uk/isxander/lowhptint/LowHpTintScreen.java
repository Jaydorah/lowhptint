package co.uk.isxander.lowhptint;

import co.uk.isxander.lowhptint.config.LowHpTintConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class LowHpTintScreen extends Screen {
    private final LowHpTintConfig config;

    public LowHpTintScreen() {
        super(Text.translatable("lowhptint.config.title"));
        this.config = LowHpTintMod.getInstance().getConfig();
    }

    @Override
    protected void init() {
        // Toggle button
        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal("Enabled: " + getFormattedBoolean(config.isEnabled())),
                        button -> {
                            config.setEnabled(!config.isEnabled());
                            button.setMessage(Text.literal("Enabled: " + getFormattedBoolean(config.isEnabled())));
                        })
                .dimensions(width / 2 - 75, getRowPos(1), 150, 20)
                .build());

        // Health threshold slider
        this.addDrawableChild(new ValueSliderWidget(
                width / 2 - 75, getRowPos(2), 150, 20,
                Text.literal("Health: "),
                0, 20, config.getHealth(),
                value -> config.setHealth((int) value)
        ));

        // Red color slider
        this.addDrawableChild(new ValueSliderWidget(
                width / 2 - 75, getRowPos(3), 150, 20,
                Text.literal("Red: "),
                0, 255, config.getRed(),
                value -> config.setRed((int) value)
        ));

        // Green color slider
        this.addDrawableChild(new ValueSliderWidget(
                width / 2 - 75, getRowPos(4), 150, 20,
                Text.literal("Green: "),
                0, 255, config.getGreen(),
                value -> config.setGreen((int) value)
        ));

        // Blue color slider
        this.addDrawableChild(new ValueSliderWidget(
                width / 2 - 75, getRowPos(5), 150, 20,
                Text.literal("Blue: "),
                0, 255, config.getBlue(),
                value -> config.setBlue((int) value)
        ));

        // Speed slider
        this.addDrawableChild(new ValueSliderWidget(
                width / 2 - 75, getRowPos(6), 150, 20,
                Text.literal("Speed: "),
                1, 20, config.getSpeed(),
                value -> config.setSpeed((int) value)
        ));

        // Done button
        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal("Done"),
                        button -> this.close())
                .dimensions(width / 2 - 75, getRowPos(7), 150, 20)
                .build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }
    @Override
    public void close() {
        config.save();
        super.close();
    }

    private int getRowPos(int rowNum) {
        return height / 4 + (24 * rowNum - 24) - 16;
    }

    private String getFormattedBoolean(boolean b) {
        return (b ? Formatting.GREEN + "ON" : Formatting.RED + "OFF");
    }

    // Custom slider widget that updates a value with a callback
    private static class ValueSliderWidget extends SliderWidget {
        private final Text prefix;
        private final double min;
        private final double max;
        private final ValueConsumer valueConsumer;

        public ValueSliderWidget(int x, int y, int width, int height, Text prefix, double min, double max, double value, ValueConsumer valueConsumer) {
            super(x, y, width, height, Text.empty(), (value - min) / (max - min));
            this.prefix = prefix;
            this.min = min;
            this.max = max;
            this.valueConsumer = valueConsumer;
            this.updateMessage();
        }

        @Override
        protected void updateMessage() {
            this.setMessage(Text.empty().append(prefix).append(String.valueOf((int) getValue())));
        }

        @Override
        protected void applyValue() {
            this.valueConsumer.consume(getValue());
        }

        private double getValue() {
            return min + (max - min) * this.value;
        }

        @FunctionalInterface
        interface ValueConsumer {
            void consume(double value);
        }
    }
}