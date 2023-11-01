package com.bell.thingdong.domain.object.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserObjectInventoryDto {
	@Schema(description = "유저가 가진 오브젝트 id", example = "2")
	private Long userObjectId;

	@Schema(description = "오브젝트 파일 경로", example = "wqexqwax")
	private String objectPath;

	@Schema(description = "유저가 가진 오브젝트의 room id", example = "2")
	private Long roomId;

	@Schema(description = "오브젝트의 가격, 0이면 언박띵", example = "20")
	private Long objectThing;

	@Schema(description = "오브젝트 구매여부", example = "Y")
	private String objectStatus;
}