package myns.histbatch.impl.watcher;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import myns.histbatch.watcher.MimeItemType;
import myns.histbatch.watcher.MimeItemTypeMatcher;

class MimeItemTypeMatcherTest {

    @Test
    void testWithFixedType() {
        String glob = "application/json";
        MimeItemTypeMatcher matcher = new MimeItemTypeMatcher(glob);
        
        Map<String, Object> data = Map.of(
                "object", new Object(),
                "string", "application/json",
                "image/jpeg", new MimeItemType("image/jpeg"),
                "text/json", new MimeItemType("text/json"),
                "application/json", new MimeItemType("application/json"),
                "x-application/json", new MimeItemType("x-application/json"));
        
        assertThat(findAll(data, matcher)).isEqualTo(Set.of(
                "application/json"));
    }

    @Test
    void testWithAny() {
        String glob = "*/*";
        MimeItemTypeMatcher matcher = new MimeItemTypeMatcher(glob);
        
        Map<String, Object> data = Map.of(
                "object", new Object(),
                "string", "application/json",
                "image/jpeg", new MimeItemType("image/jpeg"),
                "text/json", new MimeItemType("text/json"),
                "application/json", new MimeItemType("application/json"));
        
        assertThat(findAll(data, matcher)).isEqualTo(Set.of(
                "image/jpeg", "text/json", "application/json"));
    }

    @Test
    void testWithWildcard() {
        String glob = "image/*";
        MimeItemTypeMatcher matcher = new MimeItemTypeMatcher(glob);
        
        Map<String, Object> data = Map.of(
                "object", new Object(),
                "string", "application/json",
                "image/jpeg", new MimeItemType("image/jpeg"),
                "x-image/jpeg", new MimeItemType("x-image/jpeg"),
                "text/json", new MimeItemType("text/json"),
                "application/json", new MimeItemType("application/json"),
                "image/bmp", new MimeItemType("image/bmp"));
        
        assertThat(findAll(data, matcher)).isEqualTo(Set.of(
                "image/jpeg", "image/bmp"));
    }

    @Test
    void testWithPartialWildcard() {
        String glob = "application/*ml";
        MimeItemTypeMatcher matcher = new MimeItemTypeMatcher(glob);
        
        Map<String, Object> data = Map.of(
                "object", new Object(),
                "string", "application/json",
                "image/jpeg", new MimeItemType("image/jpeg"),
                "text/json", new MimeItemType("text/json"),
                "text/html", new MimeItemType("text/html"),
                "application/xhtml-xml", new MimeItemType("application/xhtml-xml"),
                "text/yaml", new MimeItemType("text/yaml"),
                "application/yaml", new MimeItemType("application/yaml"),
                "application/yaml-xxx", new MimeItemType("application/yaml-xxx"),
                "application/toml", new MimeItemType("application/toml"));
        
        assertThat(findAll(data, matcher)).isEqualTo(Set.of(
                "application/xhtml-xml", "application/yaml", "application/toml"));
    }

    @Test
    void testWithPrefixPattern() {
        Pattern pattern = Pattern.compile("^text/");
        MimeItemTypeMatcher matcher = new MimeItemTypeMatcher(pattern);
        
        Map<String, Object> data = Map.of(
                "object", new Object(),
                "string", "text/plain",
                "text/jpeg", new MimeItemType("mage/jpeg"),
                "text/plain", new MimeItemType("text/plain"));
        
        assertThat(findAll(data, matcher)).isEqualTo(Set.of(
                "text/plain"));
    }

    @Test
    void testWithFullPattern() {
        Pattern pattern = Pattern.compile("^image/(?:jpeg|png)$");
        MimeItemTypeMatcher matcher = new MimeItemTypeMatcher(pattern);
        
        Map<String, Object> data = Map.of(
                "object", new Object(),
                "string", "image/png",
                "text/plain", new MimeItemType("text/plain"),
                "image/bmp", new MimeItemType("image/bmp"),
                "image/jpeg", new MimeItemType("image/jpeg"),
                "image/png", new MimeItemType("image/png"));
        
        assertThat(findAll(data, matcher)).isEqualTo(Set.of(
                "image/jpeg", "image/png"));
    }

    
    private static Set<String> findAll(Map<String, Object> data, MimeItemTypeMatcher matcher) {
        return data.entrySet().stream()
                .filter(entry -> matcher.test(entry.getValue()))
                .map(entry -> entry.getKey())
                .collect(Collectors.toSet());
    }

}
