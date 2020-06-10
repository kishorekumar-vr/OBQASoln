package util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConfigTemplateUtils {

    private static final String TEST_NAME_TAG = "{{TEST_NAME}}";
    private static final String UUID_TAG = "{{UUID}}";
    private static final String TEST_NAME_DELIMITERSLESS_TAG = "{{TEST_NAME_DELIMITERSLESS}}";
    private static final String TEST_NAME_DELIMITERSLESS_18_TAG = "{{TEST_NAME_DELIMITERSLESS_18}}";
    private static final String TEST_NAME_DELIMITERSLESS_16_TAG = "{{TEST_NAME_DELIMITERSLESS_16}}";
    private static final String UUID_DELIMITERSLESS_TAG = "{{UUID_DELIMITERSLESS}}";
    private static final String UUID_DELIMITERSLESS_31_TAG = "{{UUID_DELIMITERSLESS_31}}";
    private static final String UUID_DELIMITERSLESS_30_TAG = "{{UUID_DELIMITERSLESS_30}}";
    private static final String UUID_DELIMITERSLESS_18_TAG = "{{UUID_DELIMITERSLESS_18}}";
    private static final String TEMPLATE_REGEX = ".*(\\{\\{.*\\}\\}).*";

    public void parseOutTemplatedValues(final String testCaseId, final Object object) throws IllegalAccessException {
        if (object != null) {
            final Field[] fieldsOnObject = object.getClass().getDeclaredFields();

            Arrays.stream(fieldsOnObject).forEach((ThrowingConsumer<Field>) field -> {

                field.setAccessible(true);
                if (this.isStringType(field)) {

                    final String possibleFieldValue = (String) field.get(object);

                    if (this.containsTemplate(possibleFieldValue)) {

                        field.set(object, this.parseTemplate(testCaseId, possibleFieldValue));
                    }

                }else if(this.isStringValueMap(object)){
                    final Map<Object,String> mapType = (Map<Object, String>) object;

                    mapType.keySet()
                            .forEach(key -> {
                                final String rawValue = mapType.get(key);
                                final String parsedValue = this.parseTemplate(testCaseId,rawValue);
                                mapType.put(key,parsedValue);
                            });
                } else if(this.isStringValueList(object)) {
                    final List<String>  listType = (List<String>) object;

                    final List<String> parsedList = listType.stream()
                            .map(rawVal -> this.parseTemplate(testCaseId, rawVal))
                            .collect(Collectors.toList());

                    listType.clear();
                    listType.addAll(parsedList);
                }else if (this.isNotForbiddenForParsingType(field)){
                    this.parseOutTemplatedValues(testCaseId,field.get(object));
                }
            });
        }
    }

    private boolean isStringValueList(final Object object){
        if(List.class.isAssignableFrom(object.getClass())
            && ((List<Object>) object).size()>0) {

            final Object element = ((List<Object>) object).get(0);

            return isStringType(element.getClass().getName());
        }
        return false;
    }

    private boolean isStringType(final Field field) {
        final String fieldType = field.getType().getName();
        field.getName();
        return isStringType(fieldType);
    }

    private boolean isStringType(final String typeName) {
        return StringUtils.equalsIgnoreCase(typeName, "java.lang.string");
    }

    private boolean containsTemplate(final String possibleTemplate) {
        return StringUtils.isNotBlank(possibleTemplate) && possibleTemplate.matches(TEMPLATE_REGEX);
    }

    private boolean isStringValueMap(final Object object){
        if(Map.class.isAssignableFrom(object.getClass())){
            final Set<Object> keyset = ((Map<Object,Object>) object).keySet();

            if(!CollectionUtils.isEmpty(keyset)){
                final Object[] keysetAsArray = keyset.toArray();

                return isStringType(keysetAsArray[0].getClass().getName());
            }
        }
        return false;
    }
    private String parseTemplate(final String testCaseId, String template) {

        final String currentTestName = StringUtils.isEmpty(testCaseId) ? "unknown" : testCaseId;

        template = StringUtils.replace(template, TEST_NAME_TAG, currentTestName);
        template = StringUtils.replace(template, UUID_TAG, UUID.randomUUID().toString());
        template = StringUtils.replace(template, TEST_NAME_DELIMITERSLESS_TAG, currentTestName.replace("-", "").replace("_", ""));
        template = StringUtils.replace(template, UUID_DELIMITERSLESS_TAG, UUID.randomUUID().toString().replace("-", "").replace("_", ""));
        template = StringUtils.replace(template, UUID_DELIMITERSLESS_31_TAG, UUID.randomUUID().toString().replace("-", "").replace("_", "").substring(0, 31));
        template = StringUtils.replace(template, UUID_DELIMITERSLESS_30_TAG, UUID.randomUUID().toString().replace("-", "").replace("_", "").substring(0, 30));
        template = StringUtils.replace(template, UUID_DELIMITERSLESS_18_TAG, UUID.randomUUID().toString().replace("-", "").replace("_", "").substring(0, 18));
        template = StringUtils.replace(template, TEST_NAME_DELIMITERSLESS_18_TAG, UUID.randomUUID().toString().replace("-", "").replace("_", "").substring(0, 18));
        template = StringUtils.replace(template, TEST_NAME_DELIMITERSLESS_16_TAG, UUID.randomUUID().toString().replace("-", "").replace("_", "").substring(0, 16));

        final Pattern templateTagPattern = Pattern.compile(TEMPLATE_REGEX);
        Matcher matcher = templateTagPattern.matcher(template);
        if (matcher.matches()) {
            throw new RuntimeException("Unparsed template string(s) in:" + template);
        } else {
            return template;
        }
    }

    private boolean isNotForbiddenForParsingType(final Field field){
        final String fieldType = field.getType().getName();

        return !(field.getType().isEnum()
                || field.getType().isArray()
                || field.getType().isPrimitive()
                || StringUtils.equalsIgnoreCase("java.math.BigDecimal", fieldType)
                || StringUtils.equalsIgnoreCase("java.math.Integer", fieldType)
                || StringUtils.equalsIgnoreCase("java.math.Boolean", fieldType));

    }

}

