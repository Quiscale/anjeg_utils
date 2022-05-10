package net.anjeg.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.media.Media;
import javafx.scene.text.Font;

public class Loader {

	// /////////////////////////////////////////////////////////////////////////////
	// Attributes
	// /////////////////////////////////////////////////////////////////////////////

	private Map<String, Object> resources;
	private String rootPath;
	
	// /////////////////////////////////////////////////////////////////////////////
	// Constructors
	// /////////////////////////////////////////////////////////////////////////////

	/**
	 * 
	 * @param rootPath (ex: assets/.../)
	 */
	public Loader(String rootPath) {
		super();
		
		this.resources = new HashMap<>();
		this.rootPath = rootPath;
		
		this.loadJar();
	}
	
	// /////////////////////////////////////////////////////////////////////////////
	// Methods
	// /////////////////////////////////////////////////////////////////////////////

	/**
	 * Load resources in jar files
	 */
	@SuppressWarnings("unchecked")
	private void loadJar() {
		
		ClassLoader cl = Loader.class.getClassLoader();
		InputStream is = cl.getResourceAsStream(this.rootPath + "resources.json");
		
		try {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(new InputStreamReader(is));

			// Load textures
			if(json.containsKey("texture")) {
				System.out.println("[loader] load textures");
				JSONObject subJson = (JSONObject) json.get("texture");
				subJson.forEach((rKey, rValue) -> {
					//System.out.println("loading " + rKey);
					Image image = new Image(cl.getResourceAsStream(this.rootPath + "texture/" + rValue));
					this.resources.put((String) rKey, image);
				});
			}

			// Load fonts
			if(json.containsKey("font")) {
				System.out.println("[loader] load fonts");
				JSONObject subJson = (JSONObject) json.get("font");
				subJson.forEach((rKey, rValue) -> {
					Font font = Font.loadFont(cl.getResourceAsStream(this.rootPath + "font/" + rKey), (Long) rValue);
					this.resources.put((String) rKey, font);
					//System.out.println(rKey + " loaded");
				});
			}

			// Load sounds
			if(json.containsKey("sound")) {
				System.out.println("[loader] load sounds");
				JSONObject subJson = (JSONObject) json.get("sound");
				subJson.forEach((rKey, rValue) -> {
					Media media = new Media(cl.getResource(this.rootPath + "font/" + rKey).toExternalForm());
					this.resources.put((String) rKey, media);
					//System.out.println(rKey + " loaded");
				});
			}
			
			is.close();
			
		}
		catch (Exception e) {
			System.err.println("[loader] not able to load json or things in : ");
			e.printStackTrace();
		}
		
		
		System.out.println("[loader] jar resources loaded");
	}

	public Resource get(String name) {
		
		//System.out.println("[loader] get : " + name);
		
		Optional<Entry<String, Object>> result = resources.entrySet()
				.stream()
				.filter( k -> k.getKey().endsWith(name))
				.findFirst();
		
		if(result.isPresent())
			return new Resource(result.get().getValue());
		else
			return null;
		
	}
	
	public Image resized(String id, double scaleFactor) {
		final Image input = this.get(id).asImage();
		final int W = (int) input.getWidth();
		final int H = (int) input.getHeight();
		final int S = (int) scaleFactor;

		WritableImage output = new WritableImage(W * S, H * S);

		PixelReader reader = input.getPixelReader();
		PixelWriter writer = output.getPixelWriter();

		for (int y = 0; y < H; y++) {
			for (int x = 0; x < W; x++) {
				final int argb = reader.getArgb(x, y);
				for (int dy = 0; dy < S; dy++) {
					for (int dx = 0; dx < S; dx++) {
						writer.setArgb(x * S + dx, y * S + dy, argb);
					}
				}
			}
		}
		
		return output;
	}
	
	// /////////////////////////////////////////////////////////////////////////////
	// Override
	// /////////////////////////////////////////////////////////////////////////////

	// /////////////////////////////////////////////////////////////////////////////
	// Class
	// /////////////////////////////////////////////////////////////////////////////

	public static class Resource {
		
		private Object object;
		
		public Resource(Object object) {
			this.object = object;
		}
		
		public Image asImage() {
			return (Image) object;
		}
		
		public Font asFont() {
			return (Font) object;
		}
		
		public Media asMedia() {
			return (Media) object;
		}
		
	}
	
}
