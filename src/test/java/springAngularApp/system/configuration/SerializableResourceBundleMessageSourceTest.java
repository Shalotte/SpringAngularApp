package springAngularApp.system.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Locale;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class SerializableResourceBundleMessageSourceTest {

    private static final String FIRST_TEST_PROPERTIES_FILE_NAME = "messages_test";
    private static final String SECOND_TEST_PROPERTIES_FILE_NAME = "messages_test2";
    private static final String FIRST_TEST_PROPERTY_FILE_PROPERTY_NAME = "test1";
    private static final String SECOND_TEST_PROPERTY_FILE_PROPERTY_NAME = "test2";

    @InjectMocks private SerializableResourceBundleMessageSource testee;

    @Test
    public void getAllProperties_TwoFiles_PropertiesFromBothFilesWereReturned() {
        testee.setBasenames(FIRST_TEST_PROPERTIES_FILE_NAME, SECOND_TEST_PROPERTIES_FILE_NAME);

        Properties properties = testee.getAllProperties(Locale.getDefault());

        assertThat(properties.containsKey(FIRST_TEST_PROPERTY_FILE_PROPERTY_NAME)).isTrue();
        assertThat(properties.containsKey(SECOND_TEST_PROPERTY_FILE_PROPERTY_NAME)).isTrue();
    }

    @Test
    public void getAllProperties_RestrictedToTheFirstFile_PropertiesOnlyFromTheFirstFileWereReturned() {
        initTestObject("messages/test1");

        testee.setBasenames(FIRST_TEST_PROPERTIES_FILE_NAME);

        Properties properties = testee.getAllProperties(Locale.getDefault());

        assertThat(properties.containsKey(FIRST_TEST_PROPERTY_FILE_PROPERTY_NAME)).isTrue();
        assertThat(properties.containsKey(SECOND_TEST_PROPERTY_FILE_PROPERTY_NAME)).isFalse();
    }

    @Test
    public void getAllProperties_RestrictedToTheSecondFile_PropertiesOnlyFromTheSecondFileWereReturned() {
        initTestObject("messages/test2");

        Properties properties = testee.getAllProperties(Locale.getDefault());

        assertThat(properties.containsKey(FIRST_TEST_PROPERTY_FILE_PROPERTY_NAME)).isFalse();
        assertThat(properties.containsKey(SECOND_TEST_PROPERTY_FILE_PROPERTY_NAME)).isTrue();
    }

    @Test
    public void getAllProperties_WrongPropertySourceDirectory_EmptyPropertiesListHasBeenReturned() {
        initTestObject("invalidFolder");

        Properties properties = testee.getAllProperties(Locale.getDefault());

        assertThat(properties).isEmpty();
    }

    private void initTestObject(String messageSource) {
        testee = new SerializableResourceBundleMessageSource(){
            @Override
            protected String getMessageSource() {
                return messageSource;
            }
        };
        initMocks(this);
    }

}