package com.motorcycleparts.motorcycleparts_master;

import com.motorcycleparts.motorcycleparts_master.auth.AuthenticationService;
import com.motorcycleparts.motorcycleparts_master.auth.RegisterRequest;
import com.motorcycleparts.motorcycleparts_master.model.*;
import com.motorcycleparts.motorcycleparts_master.model.user.Role;

import com.motorcycleparts.motorcycleparts_master.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class MotorcyclePartsMainClass {

	public static void main(String[] args) {
		SpringApplication.run(MotorcyclePartsMainClass.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService authenticationService,
			SparePartsTypeRepository sparePartsTypeRepository,
			BrandPartsRepository brandPartsRepository,
			SparePartsRepository sparePartsRepository,
			MotorTypeRepository motorTypeRepository,
			BrandMotorRepository brandMotorRepository,
			Parts_MotorTypeRepository parts_motorTypeRepository,
			ReviewsRepository reviewsRepository,
			CustomerRepository customerRepository,
			DiscountRepository discountRepository
	){
	return args -> {
		var admin = RegisterRequest.builder()
				.firstName("admin")
				.lastName("admin")
				.email("admin@admin.com")
				.password("admin")
				.role(Role.ADMIN).build();
		System.out.println("Admin token: "+authenticationService.register(admin).getAccess_token());

		var customer = RegisterRequest.builder()
				.firstName("user")
				.lastName("user")
				.email("huynhname32@gmail.com")
				.password("1")
				.build();
		authenticationService.register(customer);

//			BrandParts b = BrandParts.builder().name("Profender X  Series").build();
//			brandPartsRepository.save(b);
//			BrandParts b1 = BrandParts.builder().name("Deli").build();
//			brandPartsRepository.save(b1);
//			BrandParts b2 = BrandParts.builder().name("Profender X  Series 1").build();
//			brandPartsRepository.save(b2);
//			BrandParts b3 = BrandParts.builder().name("Profender X  Series 2").build();
//			brandPartsRepository.save(b3);
//			BrandParts b4 = BrandParts.builder().name("Profender X  Series 3").build();
//			brandPartsRepository.save(b4);
//
//			SparePartsType spt = SparePartsType.builder().name("Căm xe").build();
//			sparePartsTypeRepository.save(spt);
//			SparePartsType spt1 = SparePartsType.builder().name("Vỏ xe").build();
//			sparePartsTypeRepository.save(spt1);
//			SparePartsType spt2 = SparePartsType.builder().name("Phuộc xe").build();
//			sparePartsTypeRepository.save(spt2);
//			SparePartsType spt3 = SparePartsType.builder().name("Yên xe").build();
//			sparePartsTypeRepository.save(spt3);
//			SparePartsType spt4 = SparePartsType.builder().name("Pô xe").build();
//			sparePartsTypeRepository.save(spt4);
//			SparePartsType spt5 = SparePartsType.builder().name("Thắng xe").build();
//			sparePartsTypeRepository.save(spt5);
//
//
//			SpareParts spareParts = SpareParts.builder()
//					.brandParts(b)
//					.sparePartsType(spt)
//					.inventory(121214)
//					.name("Phuộc Profender X Series cho SHVN")
//					.sellNumber(1244)
//					.wattage(null)
//					.voltage(null)
//					.start(4.0f)
//					.size("100/90-10")
//					.averageLifespan(3.5f)
//					.year(2021)
//					.origin("Thái Lan")
//					.unitPrice(319.9f)
//					.warranty(1.5f)
//					.iso("ISO 9001:2015")
//
//					//phuộc lò xo
//					.type("Phuộc lồng")
//					.image("C:\\Users\\huynh\\OneDrive\\Hình ảnh\\Acer\\Acer_Wallpaper_02_3840x2400.jpg")
//					.weight(2.5f)
//					.description("Phuộc Profender X Series cho PCX 160 hàng chính hãng Profender sản xuất tại Thái Lan." +
//							" Phuộc Profender X Series cho Honda PCX là dòng phuộc có bình dầu phụ, tặng kèm 2 loxo phụ, 2 nút tăng chỉnh rebound và độ nhún, chân phuộc 16 nấc hiển thị số rõ ràng , " +
//							"dễ tăng chỉnh có thể giúp bạn tùy chỉnh khi đi phố, khi chở nặng...").build();
//
//			sparePartsRepository.save(spareParts);
//
//
//			SpareParts spareParts1 = SpareParts.builder()
//					.brandParts(b1)
//					.sparePartsType(spt1)
//					.inventory(121214)
//					.name("Vỏ Deli 100/90-10 Urban Grip")
//					.sellNumber(1244)
//					.unitPrice(319.9f)
//					.year(2020)
//					.start(4.5f)
//					.origin("Indonesia")
//					.averageLifespan(2.5f)
//					.size("100/90-10")
//					.warranty(1.5f)
//					.weight(2.5f)
//					.type("Vỏ ruột đặc")
//					.iso("ISO 9001:2015")
//					.voltage(null)
//					.wattage(null)
//					.image("C:\\Users\\huynh\\OneDrive\\Hình ảnh\\Acer\\Acer_Wallpaper_02_3840x2400.jpg")
//					.description("Vỏ xe Deli 100/90-10 Urban Grip mã gai SC-109, Deli là thương hiệu mẹ của vỏ xe Swallow sản xuất tại Indonesia đã quen thuộc tại Việt Nam," +
//							"trước giờ chủ yếu làm vỏ xe du lịch, xe đạp, xe địa hình...nay đã có vỏ dành cho xe 2 bánh").build();
//			sparePartsRepository.save(spareParts1);
//
			BrandMotor brandMotor = BrandMotor.builder().name("Honda").build();
			brandMotorRepository.save(brandMotor);
			BrandMotor brandMotor1 = BrandMotor.builder().name("Yamaha").build();
			brandMotorRepository.save(brandMotor1);
//			BrandMotor brandMotor2 = BrandMotor.builder().name("Honda2").build();
//			brandMotorRepository.save(brandMotor2);
//			BrandMotor brandMotor3 = BrandMotor.builder().name("Honda3").build();
//			brandMotorRepository.save(brandMotor3);
//
//
//			MotorType motorType = MotorType.builder().name("SH").carYear("2021").brandMotor(brandMotor).build();
//			motorTypeRepository.save(motorType);
//			MotorType motorType1 = MotorType.builder().name("Future").carYear("2017").brandMotor(brandMotor).build();
//			motorTypeRepository.save(motorType1);
//
//
//			Parts_MotorType parts_motorType = Parts_MotorType.builder()
//					.motorType(motorTypeRepository.findById(1L).get())
//					.spareParts(sparePartsRepository.findById(2L).get())
//					.build();
//			parts_motorTypeRepository.save(parts_motorType);
//
//			Parts_MotorType parts_motorType1 = Parts_MotorType.builder()
//					.motorType(motorTypeRepository.findById(1L).get())
//					.spareParts(sparePartsRepository.findById(1L).get())
//					.build();
//			parts_motorTypeRepository.save(parts_motorType1);
//
//
//			Parts_MotorType parts_motorType2 = Parts_MotorType.builder()
//					.motorType(motorTypeRepository.findById(2L).get())
//					.spareParts(sparePartsRepository.findById(1L).get())
//					.build();
//			parts_motorTypeRepository.save(parts_motorType2);

//			var manager = RegisterRequest.builder()
////					.firstName("manager")
////					.lastName("manager")
////					.email("manager@gmail.com")
////					.password("password")
////					.role(Role.MANAGER).build();
////			System.out.println("manager token: "+authenticationService.register(manager).getAccess_token());


//			for(int i = 1; i <= 20; i++) {
//				Reviews reviews = Reviews.builder()
//						.content("Sản phẩm chất lượng, giá cả hợp lý")
//						.rating(5)
//						.customer(customerRepository.findById(2L).get())
//						.date(null) // Sử dụng ngày hiện tại
//						.spareParts(sparePartsRepository.findById(2L).get())
//						.build(); // Thêm build() để tạo đối tượng từ builder
//				reviewsRepository.save(reviews); // Lưu đối tượng vào cơ sở dữ liệu
//			}

//			Discount discount = Discount.builder()
//				.code("DISCOUNT_1")
//				.name("Discount 1")
//				.description("Discount 1")
//				.discount(10)
////				.spareParts(List.of(sparePartsRepository.findById(1L).get()))
//				.startDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()))
//				.endDate(Date.from(LocalDate.now().plusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant()))
//				.build();
//			discountRepository.save(discount);
//
//			Discount discount1 = Discount.builder()
//				.code("DISCOUNT_2")
//				.name("Discount 2")
//				.description("Discount 2")
//				.discount(20)
////				.spareParts(List.of(sparePartsRepository.findById(2L).get()))
//				.startDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()))
//				.endDate(Date.from(LocalDate.now().plusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant()))
//				.build();
//			discountRepository.save(discount1);

	};
	}
}
