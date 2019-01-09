package me.george.daedwin.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ItemSerialization {

    /**
     * Get an itemStack from the base64String
     *
     * @param base42String
     * @return
     * @since 1.0
     */
    public static ItemStack itemStackFromBase64(String base42String) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(base42String));

            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            ItemStack items = (ItemStack) dataInput.readObject();

            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            try {
                throw new IOException("Unable to decode class type.", e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Convert one (1) item to base64 serialized String.
     *
     * @param item
     * @return
     * @throws IllegalStateException
     * @since 1.0
     */
    public static String itemStackToBase64(ItemStack item) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeObject(item);

            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    /**
     * Converts an Inventory to a string
     *
     * @param i
     * @return String
     * @since 1.0
     */

    public static String toString(Inventory i) {
        YamlConfiguration configuration = new YamlConfiguration();

        configuration.set("Title", i.getTitle());
        configuration.set("Size", i.getSize());

        for (int a = 0; a < i.getSize(); a++) {
            ItemStack s = i.getItem(a);

            if (s == null)
                s = new ItemStack(Material.AIR, 1);
            configuration.set("Contents." + a, s);
        }

        return Base64Coder.encodeString(configuration.saveToString());
    }


    /**
     * Converts String to an Inventory.
     *
     * @param s
     * @return Inventory
     * @since 1.0
     */
    public static Inventory fromString(String s) {
        if (s == null || s.isEmpty()) return null;

        YamlConfiguration configuration = new YamlConfiguration();

        try {
            configuration.loadFromString(Base64Coder.decodeString(s));

            Inventory i = Bukkit.createInventory(null, configuration.getInt("Size"), configuration.getString("Title"));

            ConfigurationSection contents = configuration.getConfigurationSection("Contents");

            contents.getKeys(false).stream().filter(index -> contents.getItemStack(index) != null).forEach(index -> i.setItem(Integer.parseInt(index), contents.getItemStack(index)));
            return i;
        } catch (InvalidConfigurationException e) {
            return null;
        }
    }

    /**
     * Conerts String to an Inventory.
     *
     * @param s
     * @return Inventory
     * @since 1.0
     */
    public static Inventory fromString(String s, int overrideSize) {
        if (s == null || s.isEmpty()) return null;

        YamlConfiguration configuration = new YamlConfiguration();

        try {
            configuration.loadFromString(Base64Coder.decodeString(s));

            Inventory i = Bukkit.createInventory(null, overrideSize, configuration.getString("Title"));

            ConfigurationSection contents = configuration.getConfigurationSection("Contents");

            contents.getKeys(false).stream().filter(index -> contents.getItemStack(index) != null && Integer.parseInt(index) < overrideSize).forEach(index -> i.setItem(Integer.parseInt(index), contents.getItemStack(index)));
            return i;
        } catch (InvalidConfigurationException e) {
            return null;
        }
    }

    public static String toBase64(Inventory inventory) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(inventory.getSize());

            // Save every element in the list
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }


    public static Inventory fromBase64(String data, String name) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt(), name);

            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }

            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
}
