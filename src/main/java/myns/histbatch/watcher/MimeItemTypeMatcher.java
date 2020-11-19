package myns.histbatch.watcher;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link Predicate} that matches to a mime type or a kind of mime types
 */
public class MimeItemTypeMatcher implements Predicate<Object> {
    
    private static final String WILDCARD_REPLACEMENT_FRAGMENT = "[^/]*";
    
    private static final Pattern WILDCARD_PATTERN = Pattern.compile("\\*");

    
    private final Pattern pattern;
    

    /**
     * Creates predicate matching to mime type or a glob
     * 
     * @param mimeGlob
     */
    public MimeItemTypeMatcher(String mimeGlob) {
        this(parseGlob(mimeGlob));
    }

    /**
     * Creates predicate matching to a {@link Pattern}
     * 
     * @param mimeGlob
     */
    public MimeItemTypeMatcher(Pattern pattern) {
        this.pattern = pattern;
    }
    
    private static Pattern parseGlob(String mimeGlob) {
        StringBuilder patternStringBuilder = new StringBuilder("^");
        Matcher matcher = WILDCARD_PATTERN.matcher(mimeGlob);
        
        int pos = 0;
        while (matcher.find()) {
            String beforePart = mimeGlob.substring(pos, matcher.start());
            if (!beforePart.isEmpty()) {
                patternStringBuilder.append(Pattern.quote(beforePart));
            }
            patternStringBuilder.append(WILDCARD_REPLACEMENT_FRAGMENT);
            pos = matcher.end();
        }
        
        String tailPart = mimeGlob.substring(pos);
        if (!tailPart.isEmpty()) {
            patternStringBuilder.append(Pattern.quote(tailPart));
        }
        patternStringBuilder.append('$');
        String patternString = patternStringBuilder.toString();
        
        return Pattern.compile(patternString);
        
    }
    
    
    @Override
    public boolean test(Object itemType) {
        if (!(itemType instanceof MimeItemType)) {
            return false;
        }
        
        MimeItemType mimeItemType = (MimeItemType) itemType;
        String mimeType = mimeItemType.mimeType();
        
        return pattern.matcher(mimeType).find();
    }
    
}
