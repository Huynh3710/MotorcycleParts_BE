package com.motorcycleparts.motorcycleparts_master;

import com.motorcycleparts.motorcycleparts_master.auth.AuthenticationService;
import com.motorcycleparts.motorcycleparts_master.auth.RegisterRequest;
import com.motorcycleparts.motorcycleparts_master.model.BrandParts;
import com.motorcycleparts.motorcycleparts_master.model.SpareParts;
import com.motorcycleparts.motorcycleparts_master.model.SparePartsType;
import com.motorcycleparts.motorcycleparts_master.model.user.Role;

import com.motorcycleparts.motorcycleparts_master.repository.BrandPartsRepository;
import com.motorcycleparts.motorcycleparts_master.repository.SparePartsRepository;
import com.motorcycleparts.motorcycleparts_master.repository.SparePartsTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class MotorcyclePartsMainClass {



	public static void main(String[] args) {
		SpringApplication.run(MotorcyclePartsMainClass.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService authenticationService,
			SparePartsTypeRepository sparePartsTypeRepository,
			BrandPartsRepository brandPartsRepository,
			SparePartsRepository sparePartsRepository
	){
	return args -> {
		var admin = RegisterRequest.builder()
				.firstName("admin")
				.lastName("admin")
				.email("admin@gmail.com")
				.password("password")
				.role(Role.ADMIN).build();
		System.out.println("Admin token: "+authenticationService.register(admin).getAccess_token());

		BrandParts b = BrandParts.builder().name("Profender X  Series").build();
		brandPartsRepository.save(b);
		SparePartsType spt = SparePartsType.builder().name("Phuộc xe").build();
		sparePartsTypeRepository.save(spt);

		SpareParts spareParts = SpareParts.builder()
											.brandParts(b)
											.sparePartsType(spt)
											.quantityRe(121214L)
											.name("Phuộc Profender X Series cho SHVN")
											.sellNumber(1244L)
											.unitPrice(319.9)
											.image("https://ik.imagekit.io/Huynh3710/PhuocXe.jpg?updatedAt=1710076247891")
											.description("Phuộc Profender X Series cho PCX 160 hàng chính hãng Profender sản xuất tại Thái Lan." +
													" Phuộc Profender X Series cho Honda PCX là dòng phuộc có bình dầu phụ, tặng kèm 2 loxo phụ, 2 nút tăng chỉnh rebound và độ nhún, chân phuộc 16 nấc hiển thị số rõ ràng , " +
													"dễ tăng chỉnh có thể giúp bạn tùy chỉnh khi đi phố, khi chở nặng...").build();
		sparePartsRepository.save(spareParts);



		BrandParts b1 = BrandParts.builder().name("Deli").build();
		brandPartsRepository.save(b1);
		SparePartsType spt1 = SparePartsType.builder().name("Vỏ xe").build();
		sparePartsTypeRepository.save(spt1);

		SpareParts spareParts1 = SpareParts.builder()
				.brandParts(b1)
				.sparePartsType(spt1)
				.quantityRe(121214L)
				.name("Vỏ Deli 100/90-10 Urban Grip")
				.sellNumber(1244L)
				.unitPrice(319.9)
				.image("https://ik.imagekit.io/Huynh3710/PhuocXe.jpg?updatedAt=1710076247891")
				.description("Vỏ xe Deli 100/90-10 Urban Grip mã gai SC-109, Deli là thương hiệu mẹ của vỏ xe Swallow sản xuất tại Indonesia đã quen thuộc tại Việt Nam," +
						"trước giờ chủ yếu làm vỏ xe du lịch, xe đạp, xe địa hình...nay đã có vỏ dành cho xe 2 bánh").build();
		sparePartsRepository.save(spareParts1);


//		List<SpareParts> listOfBrand = sparePartsRepository.findByBrandParts(1L);
//		listOfBrand.forEach(e->{
//			System.out.println(e.getId());
//		});


//
//		var manager = RegisterRequest.builder()
//				.firstName("manager")
//				.lastName("manager")
//				.email("manager@gmail.com")
//				.password("password")
//				.role(Role.MANAGER).build();
//		System.out.println("manager token: "+authenticationService.register(manager).getAccess_token());
	};
	}
}
