package br.ccsl.cogroo.tools.featurizer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import opennlp.tools.postag.ExtendedPOSDictionary;
import opennlp.tools.postag.POSDictionary;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.model.ArtifactProvider;
import opennlp.tools.util.model.ArtifactSerializer;
import opennlp.tools.util.model.UncloseableInputStream;
import br.ccsl.cogroo.dictionary.FeatureDictionaryI;

public class DefaultFeaturizerFactory extends FeaturizerFactory {

  private static final String FEATURE_DICTIONARY_ENTRY_NAME = "tags.tagdict";

  /**
   * Creates a {@link DefaultFeaturizerFactory} that provides the default
   * implementation of the resources.
   */
  public DefaultFeaturizerFactory() {
  }

  /**
   * Creates a {@link DefaultFeaturizerFactory} with an {@link ArtifactProvider} that
   * will be used to retrieve artifacts. This constructor will try to get the
   * ngram and tags dictionaries from the artifact provider.
   * <p>
   * Sub-classes should implement a constructor with this signatures and call
   * this constructor.
   * <p>
   * This will be used to load the factory from a serialized POSModel.
   */
  public DefaultFeaturizerFactory(ArtifactProvider artifactProvider) {
    super(artifactProvider);
  }

  /**
   * Creates a {@link DefaultFeaturizerFactory}. Use this constructor to
   * programmatically create a factory.
   * 
   */
  public DefaultFeaturizerFactory(FeatureDictionaryI featureDictionary) {
    super(featureDictionary);
  }

  @Override
  @SuppressWarnings("rawtypes")
  public Map<String, ArtifactSerializer> createArtifactSerializersMap() {
    Map<String, ArtifactSerializer> serializers = super
        .createArtifactSerializersMap();
    
    ExtendedTagDictionarySerializer.register(serializers);

    return serializers;
  }

  @Override
  public Map<String, Object> createArtifactMap() {
    Map<String, Object> artifactMap = super.createArtifactMap();

    if (featureDictionary != null)
      artifactMap.put(FEATURE_DICTIONARY_ENTRY_NAME, featureDictionary);

    return artifactMap;
  }

  @Override
  protected FeatureDictionaryI loadFeatureDictionary() {
    if (artifactProvider != null)
      return artifactProvider.getArtifact(FEATURE_DICTIONARY_ENTRY_NAME);
    return null;
  }

  public FeaturizerContextGenerator getFeaturizerContextGenerator() {
    return new DefaultFeaturizerContextGenerator();
  }

  @SuppressWarnings("unchecked")
  @Override
  public void validateArtifactMap() throws InvalidFormatException {

    super.validateArtifactMap();
    
    Object tagdictEntry = this.artifactProvider
        .getArtifact(FEATURE_DICTIONARY_ENTRY_NAME);

    if (tagdictEntry != null) {
      if (!(tagdictEntry instanceof FeatureDictionaryI)) {
        throw new InvalidFormatException("Feature dictionary has wrong type!");
      }
    }
  }

  static class POSDictionarySerializer implements
      ArtifactSerializer<POSDictionary> {

    public POSDictionary create(InputStream in) throws IOException,
        InvalidFormatException {
      return POSDictionary.create(new UncloseableInputStream(in));
    }

    public void serialize(POSDictionary artifact, OutputStream out)
        throws IOException {
      artifact.serialize(out);
    }

    @SuppressWarnings("rawtypes")
    static void register(Map<String, ArtifactSerializer> factories) {
      factories.put("tagdict", new POSDictionarySerializer());
    }
  }

  static class ExtendedTagDictionarySerializer implements
      ArtifactSerializer<ExtendedPOSDictionary> {

    public ExtendedPOSDictionary create(InputStream in) throws IOException,
        InvalidFormatException {
      return ExtendedPOSDictionary.create(new UncloseableInputStream(in));
    }

    public void serialize(ExtendedPOSDictionary artifact, OutputStream out)
        throws IOException {
      artifact.serialize(out);
    }

    static void register(
        @SuppressWarnings("rawtypes") Map<String, ArtifactSerializer> factories) {
      factories.put("tagdict", new ExtendedTagDictionarySerializer());
    }
  }

}