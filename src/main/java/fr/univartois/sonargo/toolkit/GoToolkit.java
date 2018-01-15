package fr.univartois.sonargo.toolkit;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;

import org.sonar.api.internal.google.common.collect.ImmutableList;
import org.sonar.colorizer.Tokenizer;
import org.sonar.sslr.toolkit.AbstractConfigurationModel;
import org.sonar.sslr.toolkit.ConfigurationProperty;
import org.sonar.sslr.toolkit.Toolkit;

import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.impl.Parser;

import fr.univartois.sonargo.core.language.GoParser;

public final class GoToolkit {

    private GoToolkit() {
    }

    public static void main(String[] args) {
	final Toolkit toolkit = new Toolkit("SonarSource : Go : Toolkit", new GoConfigurationModel());
	toolkit.run();
    }

    static class GoConfigurationModel extends AbstractConfigurationModel {

	private final ConfigurationProperty charsetProperty = new ConfigurationProperty("Charset",
		"Charset used when opening files.", "UTF-8", newValueCandidate -> {
		    try {
			Charset.forName(newValueCandidate);
			return "";
		    } catch (final IllegalCharsetNameException e1) {
			return "Illegal charset name: " + newValueCandidate;
		    } catch (final UnsupportedCharsetException e2) {
			return "Unsupported charset: " + newValueCandidate;
		    }
		});

	@Override
	public List<ConfigurationProperty> getProperties() {
	    return ImmutableList.of(charsetProperty);
	}

	@Override
	public Charset getCharset() {
	    return Charset.forName(charsetProperty.getValue());
	}

	@Override
	public Parser<Grammar> doGetParser() {
	    updateConfiguration();
	    return GoParser.create();
	}

	@Override
	public List<Tokenizer> doGetTokenizers() {
	    updateConfiguration();
	    return GoColorizer.getTokenizers();
	}

	private static void updateConfiguration() {
	    /* Construct a parser configuration object from the properties */
	}

    }

}