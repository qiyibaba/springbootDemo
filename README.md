# springbootDemo

主函数：
# @EnableTransactionManagement声明在主配置类上，表示开启声明式事务，其原理是通过@Import导入TransactionManagementConfigurationSelector组件，然后又通过TransactionManagementConfigurationSelector导入组件AutoProxyRegistrar和ProxyTransactionManagementConfiguration；
@EnableTransactionManagement
# 看得很清楚，其是一个合成体，但其中最重要的三个注解分别是：@SpringBootConfiguration，@EnableAutoConfiguration，@ComponentScan
# 这说明 @SpringBootConfiguration 也是来源于 @Configuration，二者功能都是将当前类标注为配置类，并将当前类里以 @Bean 注解标记的方法的实例注入到srping容器中，实例名即为方法名
# @EnableAutoConfiguration 借助 AutoConfigurationImportSelector 的帮助，而后者通过实现 selectImports() 方法来导出 Configuration（是不是废弃了）
# 从 ClassPath下扫描所有的 META-INF/spring.factories 配置文件，并将spring.factories 文件中的 EnableAutoConfiguration 对应的配置项通过反射机制实例化为对应标注了 @Configuration 的形式的IoC容器配置类，然后注入IoC容器
# @ComponentScan 对应于XML配置形式中的 <context:component-scan>，用于将一些标注了特定注解的bean定义批量采集注册到Spring的IoC容器之中，这些特定的注解大致包括：@Controller，@Entity，@Component，@Service，@Repository等等，对于该注解，还可以通过 basePackages 属性来更细粒度的控制该注解的自动扫描范
@SpringBootApplication

@EnableBatchTask
@EnableCaching
@MapperScan("com.lt.task")
@ComponentScan(basePackages = {"com.qiyibaba.task"})
public class MainTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainTaskApplication.class, args);
	}
}
