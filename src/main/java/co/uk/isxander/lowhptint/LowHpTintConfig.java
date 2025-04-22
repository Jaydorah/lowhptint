package co.uk.isxander.lowhptint.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LowHpTintConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("lowhptint.json");

    private boolean enabled = true;
    private int health = 5;
    private int red = 200;
    private int green = 0;
    private int blue = 0;
    private int speed = 5;

    public static LowHpTintConfig load() {
        LowHpTintConfig config = new LowHpTintConfig();

        if (Files.exists(CONFIG_PATH)) {
            try {
                config = GSON.fromJson(Files.readString(CONFIG_PATH), LowHpTintConfig.class);
            } catch (IOException e) {
                System.err.println("Failed to read LowHpTint config!");
                e.printStackTrace();
            }
        } else {
            config.save();
        }

        return config;
    }

    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(this));
        } catch (IOException e) {
            System.err.println("Failed to save LowHpTint config!");
            e.printStackTrace();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}