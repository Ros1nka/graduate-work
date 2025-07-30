package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.CommentService;

import java.util.List;

import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AdsController.class)
public class AdsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdsService adsService;

    @MockBean
    private CommentService commentService;

    @Test
    @WithMockUser
    void getAllAds_ShouldReturnAdsList() throws Exception {

        Ad ad1 = new Ad(1, "image", 1, 1, "title");
        Ad ad2 = new Ad(2, "image2", 2, 2, "title2");

        List<Ad> adList = List.of(ad1, ad2);

        Ads ads = new Ads(adList.size(), adList);

        when(adsService.getAllAds(anyString())).thenReturn(ads);

        mockMvc.perform(get("/ads")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results[0].pk").value(1))
                .andExpect(jsonPath("$.results[0].title").value("title"))
                .andExpect(jsonPath("$.results[1].pk").value(2))
                .andExpect(jsonPath("$.results[1].title").value("title2"));
    }

    @Test
    void getAllAds_Unauthorized_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/ads"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void addAd_ShouldReturnCreatedAd() throws Exception {

        CreateOrUpdateAd properties = new CreateOrUpdateAd("test", 100, "description");

        Ad expectedAd = new Ad(1, "image", 1, 100, "test");

        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        when(adsService.createAd(any(CreateOrUpdateAd.class), any(MultipartFile.class), anyString()))
                .thenReturn(expectedAd);

        mockMvc.perform(multipart("/ads")
                        .file(imageFile)
                        .param("title", properties.getTitle())
                        .param("price", String.valueOf(properties.getPrice()))
                        .param("description", properties.getDescription())
                        .with(csrf()) // Добавляем CSRF токен
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.pk").value(expectedAd.getPk()))
                .andExpect(jsonPath("$.author").value(expectedAd.getAuthor()))
                .andExpect(jsonPath("$.title").value(expectedAd.getTitle()))
                .andExpect(jsonPath("$.price").value(expectedAd.getPrice()))
                .andExpect(jsonPath("$.image").value(expectedAd.getImage()));
    }

    @Test
    @WithMockUser
    void getAds_ShouldReturnAd() throws Exception {
        ExtendedAd extendedAd = new ExtendedAd();
        extendedAd.setPk(1);

        when(adsService.getAds(anyInt())).thenReturn(extendedAd);

        mockMvc.perform(get("/ads/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(1));
    }

    @Test
    @WithMockUser
    void getAds_NotFound_ShouldReturn404() throws Exception {
        when(adsService.getAds(anyInt())).thenThrow(AdNotFoundException.class);

        mockMvc.perform(get("/ads/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void deleteAd_Forbidden_ShouldReturn403() throws Exception {
        doThrow(ForbiddenException.class).when(adsService).deleteAd(anyInt(), anyString());

        mockMvc.perform(delete("/ads/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user@example.com")
    void updateAd_ShouldReturnUpdatedAd() throws Exception {

        CreateOrUpdateAd updatedAd = new CreateOrUpdateAd();
        updatedAd.setTitle("New Title");
        updatedAd.setPrice(200);
        updatedAd.setDescription("New Description");

        Ad expectedAd = new Ad();
        expectedAd.setPk(1);
        expectedAd.setTitle("New Title");
        expectedAd.setPrice(200);

        when(adsService.updateAd(eq(1), any(CreateOrUpdateAd.class), eq("user@example.com")))
                .thenReturn(expectedAd);

        String requestJson = "{" +
                "\"title\":\"New Title\"," +
                "\"price\":200," +
                "\"description\":\"New Description\"" +
                "}";

        mockMvc.perform(patch("/ads/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(1))
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.price").value(200));
    }

    @Test
    @WithMockUser
    void getAdsMe_ShouldReturnUserAds() throws Exception {
        Ads ads = new Ads();
        ads.setCount(1);

        when(adsService.getAdsByAuthor(anyString())).thenReturn(ads);

        mockMvc.perform(get("/ads/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1));
    }

    @Test
    @WithMockUser(username = "owner@example.com")
    void updateAdImage_ShouldReturnImage() throws Exception {

        byte[] testImage = "test image".getBytes();
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                testImage
        );

        when(adsService.updateAdImage(eq(1), any(MultipartFile.class), eq("owner@example.com")))
                .thenReturn(testImage);

        mockMvc.perform(multipart("/ads/1/image")
                        .file(imageFile)
                        .with(csrf())
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(testImage));
    }
}
