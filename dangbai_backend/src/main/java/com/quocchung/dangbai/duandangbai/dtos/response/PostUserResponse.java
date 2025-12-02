package com.quocchung.dangbai.duandangbai.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostUserResponse {
    private PageResponse<List<PostResponse>> posts_pending;
}
