package tests;

import com.sbt.controllers.VKRequestController;
import com.sbt.exceptions.IdFormatException;
import com.sbt.services.VKRequestService;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestService {
    private String code = "af42d1f70757f9ec81";
    private String id = "1";

    @Mock
    public VKRequestService service;

    @Spy
    public VKRequestController controller = Mockito.mock(VKRequestController.class);

    @Test
    public void testCallAuth(){
        Mockito.when(controller.auth(code)).thenReturn(true);

        Assert.assertTrue(controller.auth(code));
        Mockito.verify(controller, Mockito.times(1)).auth(code);
        try {
            Mockito.when(service.auth(code)).thenReturn(true);
            Assert.assertTrue(service.auth(code));
            Mockito.verify(service,Mockito.times(1)).auth(code);
        } catch (ClientException | ApiException e) {
            e.printStackTrace();
        }
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
            Mockito.verify(controller, Mockito.times(1)).getInfoById(id);
        } catch (IdFormatException e) {
            e.printStackTrace();
        }

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
            Mockito.verify(controller,Mockito.times(1)).getIdByMaxLikesOnWall(id);
        } catch (IdFormatException e) {
            e.printStackTrace();
        }
    }
}
