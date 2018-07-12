package tests;

import com.sbt.controllers.VKRequestController;
import com.sbt.exceptions.IdFormatException;
import com.sbt.services.IBaseService;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.internal.verification.checkers.NumberOfInvocationsChecker;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.verification.VerificationMode;

@RunWith(MockitoJUnitRunner.class)
public class TestService {
    private String code = "ea6eea5aed2089a3ef";
    private String id = "1";
    @Mock
    private IBaseService service;

    @Spy
    private VKRequestController controller = Mockito.mock(VKRequestController.class);


    @Test
    public void testCallAuth(){
        Mockito.when(controller.auth(code)).thenReturn(true);

        Assert.assertEquals(controller.auth(code),true);


        Mockito.verify(controller, Mockito.times(1)).auth(code);

    }

    @Test
    public void testCallGetInfoById(){
        try {
            String res = "Кафедра: null\n" +
                    "Форма обучения: null\n" +
                    "Статус обучения: null\n" +
                    "Школа - \n" +
                    "Название школы: Sc.Elem. Coppino - Falletti di Barolo\n" +
                    "Года обучения 1990 - 1992\n" +
                    "Класс: \n" +
                    "Тип заведения: null\n" +
                    "Название школы: Академическая гимназия (АГ) СПбГУ\n" +
                    "Года обучения 1996 - 2001\n" +
                    "Класс: о\n" +
                    "Тип заведения: Гимназия\n" +
                    "Карьера - \n" +
                    "Название компании: null\n" +
                    "Года работы: 2006 - 2014\n" +
                    "Должность: Генеральный директор\n" +
                    "Название компании: Telegram\n" +
                    "Года работы: 2014 - null\n" +
                    "Должность: CEO\n" +
                    "Город - Санкт-Петербург";
            Mockito.when(controller.getInfoById(id)).thenReturn(res);
            Assert.assertEquals(controller.getInfoById(id),res);
        } catch (IdFormatException e) {
            e.printStackTrace();
        }
        Mockito.verify(service.getInfoUserById(id));
    }

    @Test
    public void testCallGetIdByMaxLikesOnWall(){
        String res = "Max Likes on the wall: 19\n" +
                "https://vk.com/id67890\n" +
                "https://vk.com/id117613\n" +
                "https://vk.com/id55067\n" +
                "https://vk.com/id174240\n";
        try {
            Mockito.when(controller.getIdByMaxLikesOnWall(id)).thenReturn(res);
            Assert.assertEquals(controller.getIdByMaxLikesOnWall(id),res);
            Mockito.verify(service.getIdByMaxLikesOnWall(id));
        } catch (IdFormatException | ClientException | ApiException e) {
            e.printStackTrace();
        }
    }
}
