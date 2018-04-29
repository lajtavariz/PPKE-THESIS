package hu.ppke.yeast.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QueryProcessor extends AbstractProcessor {

    @Autowired
    public QueryProcessor(ResourceLoader resourceLoader) {
        super(resourceLoader);
    }

    public List<String> getRawIndices(String query) {
        return stemWords(filterWords(getWords(query)));
    }

}
