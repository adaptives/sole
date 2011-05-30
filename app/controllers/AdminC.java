package controllers;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.yaml.snakeyaml.Yaml;

import play.Play;
import play.PlayPlugin;
import play.data.binding.Binder;
import play.data.binding.types.DateBinder;
import play.db.Model;
import play.mvc.Controller;
import play.mvc.With;
import play.test.Fixtures;
import play.vfs.VirtualFile;

@Check("admin")
@With(Secure.class)
public class AdminC extends Controller{
	
	public static void index() {
		render();
	}
	
	public static void uploadData() {
		render();
	}
	
	public static void submitData(String data) {
		Pattern keyPattern = Pattern.compile("([^(]+)\\(([^)]+)\\)");
		
		try {
			Yaml yaml = new Yaml();
			Object o = yaml.load(data);
			
	        if (o instanceof LinkedHashMap<?, ?>) {
	            @SuppressWarnings("unchecked") LinkedHashMap<Object, Map<?, ?>> objects = (LinkedHashMap<Object, Map<?, ?>>) o;
	            Map<String, Object> idCache = new HashMap<String, Object>();
	            for (Object key : objects.keySet()) {
	                Matcher matcher = keyPattern.matcher(key.toString().trim());
	                if (matcher.matches()) {
	                    String type = matcher.group(1);
	                    String id = matcher.group(2);
	                    if (!type.startsWith("models.")) {
	                        type = "models." + type;
	                    }
	                    if (idCache.containsKey(type + "-" + id)) {
	                        throw new RuntimeException("Cannot load fixture data, duplicate id '" + id + "' for type " + type);
	                    }
	                    Map<String, String[]> params = new HashMap<String, String[]>();
	                    if (objects.get(key) == null) {
	                        objects.put(key, new HashMap<Object, Object>());
	                    }
	                    serialize(objects.get(key), "object", params);
	                    @SuppressWarnings("unchecked")
	                    Class<Model> cType = (Class<Model>)Play.classloader.loadClass(type);
	                    resolveDependencies(cType, params, idCache);
	                    Model model = (Model)Binder.bind("object", cType, cType, null, params);
	                    for(Field f : model.getClass().getFields()) {
	                        // TODO: handle something like FileAttachment
	                        if (f.getType().isAssignableFrom(Map.class)) {
	                            f.set(model, objects.get(key).get(f.getName()));
	                        }
	
	                    }
	                    model._save();
	                    Class<?> tType = cType;
	                    while (!tType.equals(Object.class)) {
	                        idCache.put(tType.getName() + "-" + id, Model.Manager.factoryFor(cType).keyValue(model));
	                        tType = tType.getSuperclass();
	                    }
	                }
	            }
	        }
	        // Most persistence engine will need to clear their state
	        for(PlayPlugin plugin : Play.plugins) {
	            plugin.afterFixtureLoad();
	        }
		} catch(Exception e) {
			render("errors/505.html", e);
		}
		index();
	}
	
	private static void serialize(Map<?, ?> values, String prefix, Map<String, String[]> serialized) {
        for (Object key : values.keySet()) {
            Object value = values.get(key);
            if (value == null) {
                continue;
            }
            if (value instanceof Map<?, ?>) {
                serialize((Map<?, ?>) value, prefix + "." + key, serialized);
            } else if (value instanceof Date) {
                serialized.put(prefix + "." + key.toString(), new String[]{new SimpleDateFormat(DateBinder.ISO8601).format(((Date) value))});
            } else if (value instanceof List<?>) {
                List<?> l = (List<?>) value;
                String[] r = new String[l.size()];
                int i = 0;
                for (Object el : l) {
                    r[i++] = el.toString();
                }
                serialized.put(prefix + "." + key.toString(), r);
            } else if (value instanceof String && value.toString().matches("<<<\\s*\\{[^}]+}\\s*")) {
                Matcher m = Pattern.compile("<<<\\s*\\{([^}]+)}\\s*").matcher(value.toString());
                m.find();
                String file = m.group(1);
                VirtualFile f = Play.getVirtualFile(file);
                if (f != null && f.exists()) {
                    serialized.put(prefix + "." + key.toString(), new String[]{f.contentAsString()});
                }
            } else {
                serialized.put(prefix + "." + key.toString(), new String[]{value.toString()});
            }
        }
    }
	
	private static void resolveDependencies(Class<Model> type, Map<String, String[]> serialized, Map<String, Object> idCache) {
        Set<Field> fields = new HashSet<Field>();
        Class<?> clazz = type;
        while (!clazz.equals(Object.class)) {
            Collections.addAll(fields, clazz.getDeclaredFields());
            clazz = clazz.getSuperclass();
        }
        for (Model.Property field : Model.Manager.factoryFor(type).listProperties()) {
            if (field.isRelation) {
                String[] ids = serialized.get("object." + field.name);
                if (ids != null) {
                    for (int i = 0; i < ids.length; i++) {
                        String id = ids[i];
                        id = field.relationType.getName() + "-" + id;
                        if (!idCache.containsKey(id)) {
                            throw new RuntimeException("No previous reference found for object of type " + field.name + " with key " + ids[i]);
                        }
                        ids[i] = idCache.get(id).toString();
                    }
                }
                serialized.remove("object." + field.name);
                serialized.put("object." + field.name + "." + Model.Manager.factoryFor((Class<? extends Model>)field.relationType).keyName(), ids);
            }
        }
    }

}
