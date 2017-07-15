package com.techmali.smartteam.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Base64;

import java.util.Map;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class ObscuredSharedPreferences implements SharedPreferences {

    /**
     * The Delegate.
     */
    protected SharedPreferences delegate;
    /**
     * The Context.
     */
    protected Context context;
    /**
     * The constant UTF8.
     */
    protected static final String UTF8 = "utf-8";
    /**
     * The constant PASSWORD.
     */
    protected static final String PASSWORD = "ResearchPlus";
    private static final char[] SEKRIT = PASSWORD.toCharArray();

    /**
     * Instantiates a new Obscured shared preferences.
     *
     * @param context  the context
     * @param delegate the delegate
     */
    public ObscuredSharedPreferences(Context context, SharedPreferences delegate) {
        this.delegate = delegate;

        this.context = context;
    }

    public Editor edit() {
        return new Editor();
    }

    @Override
    public Map<String, ?> getAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        try {
            final String v = delegate.getString(key, null);
            return v != null ? Boolean.parseBoolean(decrypt(v)) : defValue;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public float getFloat(String key, float defValue) {
        try {
            final String v = delegate.getString(key, null);
            return v != null ? Float.parseFloat(decrypt(v)) : defValue;
        } catch (Exception e) {
            return 0.0f;
        }
    }

    @Override
    public int getInt(String key, int defValue) {
        try {
            final String v = delegate.getString(key, null);
            return v != null ? Integer.parseInt(decrypt(v)) : defValue;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public long getLong(String key, long defValue) {

        try {
            final String v = delegate.getString(key, null);
            return v != null ? Long.parseLong(decrypt(v)) : defValue;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public String getString(String key, String defValue) {
        try {
            final String v = delegate.getString(key, null);
            return v != null ? decrypt(v) : defValue;
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public boolean contains(String s) {
        return delegate.contains(s);
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        delegate.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        delegate.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    /**
     * Encrypt string.
     *
     * @param value the value
     * @return the string
     */
    protected String encrypt(String value) {
        try {
            final byte[] bytes = value != null ? value.getBytes(UTF8) : new byte[0];
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(SEKRIT));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID).getBytes(UTF8), 20));
            return new String(Base64.encode(pbeCipher.doFinal(bytes), Base64.NO_WRAP), UTF8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Decrypt string.
     *
     * @param value the value
     * @return the string
     */
    protected String decrypt(String value) {
        try {
            final byte[] bytes = value != null ? Base64.decode(value, Base64.DEFAULT) : new byte[0];
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(SEKRIT));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID).getBytes(UTF8), 20));
            return new String(pbeCipher.doFinal(bytes), UTF8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<String> getStringSet(String arg0, Set<String> arg1) {
        return null;
    }

    /**
     * The type Editor.
     */
    public class Editor implements SharedPreferences.Editor {

        /**
         * The Delegate.
         */
        protected SharedPreferences.Editor delegate;

        /**
         * Instantiates a new Editor.
         */
        public Editor() {
            this.delegate = ObscuredSharedPreferences.this.delegate.edit();
        }

        @Override
        public Editor putBoolean(String key, boolean value) {
            delegate.putString(key, encrypt(Boolean.toString(value)));
            return this;
        }

        @Override
        public Editor putFloat(String key, float value) {
            delegate.putString(key, encrypt(Float.toString(value)));
            return this;
        }

        @Override
        public Editor putInt(String key, int value) {
            delegate.putString(key, encrypt(Integer.toString(value)));
            return this;
        }

        @Override
        public Editor putLong(String key, long value) {
            delegate.putString(key, encrypt(Long.toString(value)));
            return this;
        }

        @Override
        public Editor putString(String key, String value) {
            delegate.putString(key, encrypt(value));
            return this;
        }

        @Override
        public void apply() {
            delegate.apply();
        }

        @Override
        public Editor clear() {
            delegate.clear();
            return this;
        }

        @Override
        public boolean commit() {
            return delegate.commit();
        }

        @Override
        public Editor remove(String s) {
            delegate.remove(s);
            return this;
        }

        @Override
        public SharedPreferences.Editor putStringSet(String arg0, Set<String> arg1) {
            return null;
        }
    }

}