package com.smart.Controller;



import java.io.File;






import java.util.*;

import java.io.File;  


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.data.web.PageableDefault;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.smart.dao.BcaStudentRepositry;

import com.smart.dao.CityRepositoy;
import com.smart.dao.CountryRepository;
import com.smart.dao.GPSRepository;

import com.smart.dao.MechCityRepository;
import com.smart.dao.MechCountryRepository;
import com.smart.dao.MechStateRepository;

import com.smart.dao.ReminderRepository;

import com.smart.dao.StateRepository;
import com.smart.dao.UserRepository;
import com.smart.dao.VendorRepository;

import com.smart.dao.addcompanyRepository;


import com.smart.dao.addProductRepository;

import com.smart.dao.addcoworkerRepository;
//import com.smart.dao.VendorRepository;
import com.smart.dao.addcustomerRepository;
import com.smart.dao.addemployeeRepository;
import com.smart.dao.addstudentRepository;
import com.smart.dao.contactRepository;
import com.smart.dto.CollegeDto;
import com.smart.dto.ColumnMetadataDto;
import com.smart.dto.CustomColumnsDTO;
import com.smart.entity.Addcompany;
import com.smart.entity.Addcustomer;
import com.smart.entity.Addemployee;
import com.smart.entity.Addstudent;
import com.smart.entity.BcaStudent;
import com.smart.entity.Cities;
import com.smart.entity.College;
import com.smart.entity.Contact;
import com.smart.entity.Coworker;
import com.smart.entity.GPS;

import com.smart.entity.MechCity;
import com.smart.entity.MechCountry;
import com.smart.entity.MechProduction;
import com.smart.entity.MechState;
import com.smart.entity.School;

import com.smart.entity.Reminder;

import com.smart.entity.States;
import com.smart.entity.User;
//import com.smart.entity.Vendor;
import com.smart.entity.Vendor;
import com.smart.service.CollegeService;
import com.smart.service.ColumnMetadataService;
import com.smart.service.MechProductionService;
import com.smart.service.SchoolService;


import com.smart.entity.addProduct;



@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
    private addProductRepository productRepository;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CountryRepository countryRepository;
	@Autowired
	private StateRepository stateRepository;
	@Autowired
	private CityRepositoy cityRepository;
	@Autowired
	private contactRepository contactRepository;

	@Autowired
	private BCryptPasswordEncoder bcPasswordEncoder;

	@Autowired
	private addemployeeRepository addemployeeRepository;
	
	@Autowired
	private addcompanyRepository addcompanyrepository;


	@Autowired
	private addcustomerRepository addcustomerRepository;

	@Autowired
	private addstudentRepository addstudentRepository;

	@Autowired
	private SearchController searchController;

	@Autowired
	private addcustomerRepository cuAddcustomerRepository;

	@Autowired
	private VendorRepository vendorRepository;

	@Autowired
	private addcoworkerRepository addcoworkerRepository;
	
	@Autowired
	private BcaStudentRepositry bcaStudentRepo;
	
	@Autowired
    private ReminderRepository reminderRepository;
    
	
	@PostMapping("/addReminder")
	public ResponseEntity<?> addReminder(@RequestBody Reminder reminder, Principal principal) {
	    try {
	        // Get the logged-in user by username from the Principal object
	        String username = principal.getName();
	        User user = userRepository.getUserByUserName(username);

	        // Set the reminder's user relationship and add the reminder to the user
	        reminder.setUser(user);
	        user.getReminders().add(reminder);

	        // Save the updated user with the new reminder in the database
	        userRepository.save(user);

	        // Return a success response
	        return ResponseEntity.ok("Reminder added successfully!");
	    } catch (Exception e) {
	        e.printStackTrace();
	        // Return an error response if something goes wrong
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add reminder");
	    }
	}
	@GetMapping("/getRemindersDue")
	public ResponseEntity<List<Reminder>> getRemindersDue(Principal principal) {
	    try {
	        String username = principal.getName();
	        User user = userRepository.getUserByUserName(username);
	        LocalDateTime now = LocalDateTime.now();

	        // Filter reminders that are due within a one-minute window of the current time
	        List<Reminder> dueReminders = user.getReminders().stream()
	                .filter(reminder -> {
	                    LocalDateTime reminderDateTime = reminder.getDateTime()
	                            .toInstant()
	                            .atZone(ZoneId.systemDefault())
	                            .toLocalDateTime();
	                    return !reminderDateTime.isAfter(now.plusMinutes(1)) &&
	                           !reminderDateTime.isBefore(now.minusMinutes(1));
	                })
	                .collect(Collectors.toList());

	        return ResponseEntity.ok(dueReminders);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}

//	@Autowired
//	private VendorRepository vendorRepository;

	@Autowired
	private ColumnMetadataService columnMetadataService;

	@Autowired
	private CollegeService collegeService;

	@Autowired
	private SchoolService schoolService;

	private Object email;
	private int studentid;
	private int employeeid;
	private Addcustomer id;

	// method adding common data to response
	@ModelAttribute
	public void addCommanData(Model model, Principal principal) {
		String username = principal.getName();
		// getuser details
		User user = userRepository.getUserByUserName(username);
		model.addAttribute("user", user);

	}




	@GetMapping("/bcaStudentList")
	public String getBcaStudentList(
	        @RequestParam(value = "name", required = false) String searchName,
	        @RequestParam(value = "educationFilter", required = false) String educationFilter,
	        @RequestParam(value = "page", defaultValue = "0") int page,
	        @RequestParam(value = "recordsPerPage", defaultValue = "5") int recordsPerPage,
	        @RequestParam(value = "sortField", defaultValue = "name") String sortField, // Default sort field
	        @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection,
	        Model model) {

	    // Add the search term, education filter, and sorting information to the model
	    model.addAttribute("searchName", searchName);
	    model.addAttribute("educationFilter", educationFilter);
	    model.addAttribute("sortField", sortField);
	    model.addAttribute("sortDirection", sortDirection);

	    // Create sorting based on the sortField and sortDirection
	    Sort sort = Sort.by(sortField).ascending();
	    if ("desc".equalsIgnoreCase(sortDirection)) {
	        sort = Sort.by(sortField).descending();
	    }

	    // Create pagination with page number and records per page
	    Pageable pageable = PageRequest.of(page, recordsPerPage, sort);
	    Page<BcaStudent> studentPage;

	    // Check if a search term or education filter is provided
	    if (searchName != null && !searchName.isEmpty() && (educationFilter != null && !educationFilter.isEmpty())) {
	        // Search by name and education filter
	        studentPage = bcaStudentRepo.findByNameContainingAndEducation(searchName, educationFilter, pageable);
	    } else if (searchName != null && !searchName.isEmpty()) {
	        // Search by name only
	        studentPage = bcaStudentRepo.findByNameContaining(searchName, pageable);
	    } else if (educationFilter != null && !educationFilter.isEmpty()) {
	        // Filter by education only
	        studentPage = bcaStudentRepo.findByEducation(educationFilter, pageable);
	    } else {
	        // No filter or search term: display default records (all students with pagination)
	        studentPage = bcaStudentRepo.findAll(pageable);
	    }

	    // Add attributes to the model for Thymeleaf template
	    model.addAttribute("bcaStudent", studentPage.getContent());
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", studentPage.getTotalPages());
	    model.addAttribute("recordsPerPage", recordsPerPage);

	    return "normal/BcaStudentList"; // Return the Thymeleaf template
	}


	    @GetMapping("/bcaStudentList/{page}")
	    public String getBcaStudentList(
	            @PathVariable int page,
	            @RequestParam(required = false) Integer recordsPerPage,
	            @RequestParam(value = "educationFilter", required = false) String educationFilter,  // Add education filter parameter
	            HttpSession session,
	            Model model) {

	        // Default records per page if none provided in session or request
	        int defaultRecordsPerPage = 5;

	        // If the user provided a custom number of records per page for the current page, save it to the session
	        if (recordsPerPage != null) {
	            session.setAttribute("recordsPerPage_" + page, recordsPerPage);
	        }

	        // Get the records per page for the current page from the session, or use default if not set
	        Integer recordsPerPageForPage = (Integer) session.getAttribute("recordsPerPage_" + page);
	        if (recordsPerPageForPage == null) {
	            recordsPerPageForPage = defaultRecordsPerPage;
	        }

	        // Create pagination with page number and custom records per page
	        Pageable pageable = PageRequest.of(page, recordsPerPageForPage);
	        Page<BcaStudent> studentPage;

	        // Check if an education filter is provided
	        if (educationFilter != null && !educationFilter.isEmpty()) {
	            studentPage = bcaStudentRepo.findByEducation(educationFilter, pageable);
	        } else {
	            studentPage = bcaStudentRepo.findAll(pageable);
	        }

	        // Add attributes to the model for Thymeleaf template
	        model.addAttribute("bcaStudent", studentPage.getContent());
	        model.addAttribute("currentPage", page);
	        model.addAttribute("totalPages", studentPage.getTotalPages());
	        model.addAttribute("recordsPerPage", recordsPerPageForPage);
	        model.addAttribute("educationFilter", educationFilter);  // To keep the filter selected after reload

	        return "normal/BcaStudentList"; // Return the Thymeleaf template
	    }
	





	@PostMapping("/{cId}/update")
	public String contactData(@PathVariable("cId") Integer cId, Model model) {
		model.addAttribute("title", "Update Contacts");
		Contact contact = contactRepository.findById(cId).get();
		model.addAttribute("contact", contact);
		model.addAttribute("countryList", countryRepository.findAll());
		model.addAttribute("cityList", cityRepository.getAllCity(contact.getStateId()));
		model.addAttribute("stateList", stateRepository.getAllState(contact.getCountryId()));
		return "normal/updata_form";
	}
	@PostMapping("/process_update")
	public String updateContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Principal principal, HttpSession session) {

		// get old contact detail
		Contact oldContactDetails = this.contactRepository.findById(contact.getcId()).get();
		try {
			if (!file.isEmpty()) {

				// deleting old contact detail image
				try {
					File deleteFile = new ClassPathResource("static/images").getFile();
					File confirmDelete = new File(deleteFile, oldContactDetails.getImage());
					confirmDelete.delete();
				} catch (Exception x) {

				}

				// adding new contact detail image
				File saveFile = new ClassPathResource("static/images").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(file.getOriginalFilename());
				User user = userRepository.getUserByUserName(principal.getName());
				contact.setUser(user);
				contactRepository.save(contact);
				session.setAttribute("message", new com.smart.helper.Message("Update Successfuly", "success"));
			} else {
				User user = userRepository.getUserByUserName(principal.getName());
				contact.setImage(oldContactDetails.getImage());
				contact.setUser(user);
				contactRepository.save(contact);
				session.setAttribute("message", new com.smart.helper.Message("Update Successfuly", "success"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/user/" + contact.getcId() + "/contact";
	}
	@PostMapping("/updateStudent")
	public String updateStudent(@RequestParam("Bid") int id,
	                            @RequestParam("profileImage") MultipartFile file,
	                            @ModelAttribute BcaStudent updatedStudent,
	                            HttpSession session) {

	    BcaStudent existingStudent = bcaStudentRepo.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("Invalid student ID: " + id));

	    try {
	        if (!file.isEmpty()) {
	            // Deleting old image
	            try {
	                File deleteFile = new ClassPathResource("static/images").getFile();
	                File confirmDelete = new File(deleteFile, existingStudent.getImage());
	                confirmDelete.delete();
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }

	            // Saving new image
	            File saveFile = new ClassPathResource("static/images").getFile();
	            Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
	            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	            existingStudent.setImage(file.getOriginalFilename());
	        } else {
	            // If no new image, keep the old one
	            existingStudent.setImage(updatedStudent.getImage());
	        }

	        // Update other fields with the new values
	        existingStudent.setName(updatedStudent.getName());
	        existingStudent.setEmail(updatedStudent.getEmail());
	        existingStudent.setDob(updatedStudent.getDob());
	        existingStudent.setCollagename(updatedStudent.getCollagename());
	        existingStudent.setStartDate(updatedStudent.getStartDate());
	        existingStudent.setEndDate(updatedStudent.getEndDate());
	        existingStudent.setEducation(updatedStudent.getEducation());

	        // Save the updated student
	        bcaStudentRepo.save(existingStudent);
	        session.setAttribute("message", new com.smart.helper.Message("Student updated successfully", "success"));

	    } catch (Exception e) {
	        e.printStackTrace();
	        session.setAttribute("message", new com.smart.helper.Message("Failed to update student", "danger"));
	    }

	    // Redirect to the student list page after updating
	    return "redirect:/user/bcaStudentList/0";
	}

	@PostMapping("/deleteStudent/{id}")
	public String deleteStudent(@PathVariable("id") BcaStudent id, RedirectAttributes redirectAttributes) {
	    // Delete the student record
	    bcaStudentRepo.delete(id);

	    // Optionally, add a success message to be shown after the redirect
	    redirectAttributes.addFlashAttribute("message", "Student deleted successfully!");

	    // Redirect back to the BCA Student List
	    return "redirect:/user/bcaStudentList/0";
	}
	


	//Product
	
	@GetMapping("/addProduct")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new addProduct());
        return "normal/addProduct";  // Assuming you have this view for adding products
    }

    // Submit product data
    @PostMapping("/submit-product")
    public String submitProduct(@ModelAttribute("product") addProduct product) {
        productRepository.save(product);  // Save the product to the database
        return "redirect:/user/showProduct"; // Redirect to the product list page
    }
 
    
//    @GetMapping("/showProduct")
//    public String showProducts(Model model,
//                               @RequestParam(value = "page", defaultValue = "0") int page,
//                               @RequestParam(value = "size", defaultValue = "10") int pageSize,
//                               @RequestParam(required = false, defaultValue = "") String category) {
//        System.out.println("Page Size: " + pageSize);  // Add this to log the page size
//        Pageable pageable = PageRequest.of(page, pageSize);
//        Page<addProduct> productPage;
//
//        if (category.isEmpty()) {
//            productPage = productRepository.findAll(pageable);
//        } else {
//            productPage = productRepository.findByCategory(category, pageable);
//        }
//
//        model.addAttribute("products", productPage.getContent());
//        model.addAttribute("currentPage", productPage.getNumber());
//        model.addAttribute("totalPages", productPage.getTotalPages());
//        model.addAttribute("pageSize", productPage.getSize());
//        model.addAttribute("category", category);
//
//        return "normal/showProduct";
//    }    
    
    @GetMapping("/showProduct")
    public String showProducts(Model model,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "10") int pageSize,
                               @RequestParam(required = false, defaultValue = "") String category,
                               @RequestParam(value = "sortField", defaultValue = "productName") String sortField,
                               @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<addProduct> productPage;

        if (category.isEmpty()) {
            productPage = productRepository.findAll(pageable);
        } else {
            productPage = productRepository.findByCategory(category, pageable);
        }

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", productPage.getNumber());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("pageSize", productPage.getSize());
        model.addAttribute("category", category);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);

        return "normal/showProduct";
    }



 // Method to display the update form with pre-filled data
    @GetMapping("/update-product/{id}")
    public String showUpdateProductForm(@PathVariable("id") int id, Model model) {
        addProduct product = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product); // Add product to model to pre-fill the form
        return "normal/updateProduct"; // New template for updating the product
    }

    // Method to handle the update form submission
    @PostMapping("/update-product/{id}")
    public String updateProduct(@PathVariable("id") int id, @ModelAttribute("product") addProduct updatedProduct) {
    	
        // Fetch the existing product from the database
        addProduct existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        
        // Update product fields with the form data
        existingProduct.setProductName(updatedProduct.getProductName());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setDescription(updatedProduct.getDescription());
        
        // Save the updated product back to the database
        productRepository.save(existingProduct);

        // Redirect to the product list page after updating
        return "redirect:/user/showProduct";  // This should map to the method showing the product list
    }


    @GetMapping("/delete-product/{id}")
    public String deleteProduct(@PathVariable("id") int id) {
        // Check if product exists before deleting (optional but good practice)
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id); // Delete product by id
        }
        return "redirect:/user/showProduct"; // Redirect to the product list page
    }
	
	
//	@GetMapping("/bcaStudentList/{page}")
//	public String getBcaStudentList(@PathVariable("page") Integer page, Model model, Principal principal) {
//	    // Get the logged-in user's username
//	    String userName = principal.getName();
//	    
//	    // Fetch the user from the database using the username
//	    User user = userRepository.getUserByUserName(userName);
//	    
//	    // Create a Pageable object to specify pagination parameters
//	    Pageable pageable = PageRequest.of(page, 10); // Adjust the size as needed
//	    
//	    // Fetch the BCA students for the logged-in user with pagination
//	    Page<BcaStudent> bcaStudentsPage = bcaStudentRepo.findByUser(user, pageable);
//	    
//	    // Add the list of BCA students to the model
//	    model.addAttribute("bcaStudents", bcaStudentsPage.getContent());
//	    
//	    // Add pagination and heading information to the model
//	    model.addAttribute("currentPage", page);
//	    model.addAttribute("totalPages", bcaStudentsPage.getTotalPages());
//	    model.addAttribute("heading", "BCA Student List");
//	    
//	    // Return the view name to display the BCA student list
//	    return "normal/BcaStudentList"; // Your HTML page name
//	}

	@GetMapping("/BcaStudent")
	public String displayBcaStudentPage(Model model) {
	    // Adding attributes to the model, similar to the "add_contact" method
	    model.addAttribute("title", "BCA Student Page"); // Title of the page
	    model.addAttribute("bcaStudent", new BcaStudent()); // New BcaStudent object for form binding
	    model.addAttribute("heading", "Add BCA Student"); // Heading for the page

	    return "normal/BcaStudent2";  // Returning the Thymeleaf template 'BcaStudent2.html' in 'normal' folder
	}
	

	// home dashboard
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {
		model.addAttribute("title", "User Dashboard");
		model.addAttribute("heading", "User Dashboard ");
		String username = principal.getName();
		System.err.println("username = " + username);

		// getuser details

		User user = userRepository.getUserByUserName(username);

		model.addAttribute("user", user);
		return "normal/user_dashboard";
	}

	@GetMapping("/hello")
	public List<Addcustomer> helloWorld() {

		return cuAddcustomerRepository.findAll();
	}

	@GetMapping("/add_contact")
	public String addContact(Model model) {
		model.addAttribute("title", "About");
		model.addAttribute("contact", new Contact());
		model.addAttribute("countryList", countryRepository.findAll());
		model.addAttribute("heading", "Add Contact ");

		return "normal/add_contact_form";
	}

	@GetMapping("/add_customer")
	public String addCustomer(Model model) {
		model.addAttribute("title", "Add customer");
		model.addAttribute("customer", new Addcustomer());
		model.addAttribute("heading", "Add Customer");

		return "normal/add_customer";
	}

	@PostMapping("/process_customer")
	public String customer_process(@ModelAttribute Addcustomer addcustomer, Principal principal, HttpSession session) {
		try {
			String name = principal.getName();
			User user = userRepository.getUserByUserName(name);

			addcustomer.setUser(user);
			user.getAddecustomer().add(addcustomer);
			userRepository.save(user);
			System.err.println("user Data " + addcustomer);
			session.setAttribute("message", new com.smart.helper.Message("Your customer added", "success"));
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new com.smart.helper.Message("something went wrong", "danger"));
		}
		return "normal/add_customer";
	}

	// show customer
	@GetMapping("/show_customer/{page}")
	public String showCustomer(@PathVariable(name = "page") Integer page,
			@RequestParam(name = "customername", required = false) String customerName, Model model,
			Principal principal) {
		model.addAttribute("title", "View Customer");
		model.addAttribute("heading", "View Customer");
		String userName = principal.getName();
		User user = userRepository.getUserByUserName(userName);

		Pageable pageable = PageRequest.of(page, 2);

		Page<Addcustomer> addcustomer;
		if (customerName != null) {
			addcustomer = addcustomerRepository.findByNameContainAndUser(customerName, user, pageable);
		} else {
			addcustomer = addcustomerRepository.findCustomerByUser(user.getId(), pageable);
		}

		model.addAttribute("customer", addcustomer.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", addcustomer.getTotalPages());
		
		return "normal/show_customer";
	}

	// update employee
	@RequestMapping("/{employeeid}/edit1")
	public String showUpdateForm1(@PathVariable Integer employeeid, Model model) {
		Addemployee employee = addemployeeRepository.findById(employeeid)
				.orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + employeeid));

		model.addAttribute("employee", employee);
		model.addAttribute("heading", "Update Employee");
		return "/normal/update_emp";

	}

	@RequestMapping("/update_customer")
	public String updatecust(@ModelAttribute Addcustomer newcustomer, @RequestParam Integer customerid,
			Principal principal) {
		Addcustomer customer = addcustomerRepository.findById(employeeid)
				.orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + employeeid));

		String name = principal.getName();
		User user = userRepository.getUserByUserName(name);

		customer.setName(newcustomer.getName());
		customer.setEmail(newcustomer.getEmail());
		customer.setName(newcustomer.getName());
		customer.setMobileno(newcustomer.getMobileno());

		addcustomerRepository.save(newcustomer);

		customer.setUser(user);
		userRepository.save(user);

		return "redirect:/user/show_customer/0";
	}
	@PostMapping("/addBcaStudent")
	public String processBcaStudent(@ModelAttribute BcaStudent bcaStudent,@RequestParam("profileImage") MultipartFile file, Principal principal, HttpSession session) {
	    try {
	        // Get the logged-in user
	        String name = principal.getName();
	        User user = userRepository.getUserByUserName(name);
             // Set the user for the BCA student
	        if (!file.isEmpty()) {

	        	bcaStudent.setImage(file.getOriginalFilename());
				File saveFile = new ClassPathResource("static/images").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
	        bcaStudent.setUser(user);
             // Save the BCA student to the user's list
	        user.getBcaStudents().add(bcaStudent);
            // Save the user with the new BCA student
	        userRepository.save(user);
	        System.err.println("user Data " +bcaStudent );
           // Success message
	        session.setAttribute("message", new com.smart.helper.Message("BCA Student added successfully!", "success"));
	    } catch (Exception e) {
	        e.printStackTrace();
	        // Error message
	        session.setAttribute("message", new com.smart.helper.Message("Something went wrong!", "danger"));
	    }

	    return "normal/BcaStudent2"; // return to the form page after processing
	}

	// process to add contact
	@PostMapping("/process_contact")
	public String contact_process(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Principal principal, HttpSession session) {
		try {
			String name = principal.getName();
			User user = userRepository.getUserByUserName(name);
			if (!file.isEmpty()) {

				contact.setImage(file.getOriginalFilename());
				File saveFile = new ClassPathResource("static/images").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
			contact.setUser(user);
			user.getContact().add(contact);
			userRepository.save(user);
			System.err.println("user Data " + contact);
			session.setAttribute("message", new com.smart.helper.Message("Your Contact added", "success"));
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new com.smart.helper.Message("something went wrong", "danger"));
		}
		return "normal/add_contact_form";
	}

	

	@GetMapping("/show_contact/{page}")
	public String showContact(@PathVariable(name = "page") Integer page,
			@RequestParam(name = "contact_name", required = false) String contactName, Model model,
			Principal principal) {
		model.addAttribute("title", "View Contacts");
		model.addAttribute("heading", "View Contacts");
		String userName = principal.getName();
		User user = userRepository.getUserByUserName(userName);

		Pageable pageable = PageRequest.of(page, 2);

		Page<Contact> contacts;
		if (contactName != null) {
			contacts = contactRepository.findByNameContainingAndUser(contactName, user, pageable);
		} else {
			contacts = contactRepository.findContactByUser(user.getId(), pageable);
		}

		model.addAttribute("contacts", contacts.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());

		return "normal/show_contacts";
	}

	// show specific contact
	@RequestMapping("/{cId}/contact")
	public String showPerticularContact(@PathVariable("cId") Integer cId, Model model, Principal principal) {
		Optional<Contact> contactOptional = contactRepository.findById(cId);
		Contact contact = contactOptional.get();
		String username = principal.getName();
		User user = userRepository.getUserByUserName(username);
		if (user.getId() == contact.getUser().getId()) {
			model.addAttribute("contact", contact);
		}
		return "normal/contact_detail";
	}

	// show specific contact by name
	@RequestMapping("/serach_contact_name")
	public String listofContactByName(Model model, Principal principal,
			@RequestParam("contact_name") String contactName) {
		String username = principal.getName();
		User user = userRepository.getUserByUserName(username);
		List<Contact> contactList = contactRepository.findByNameContainingAndUser(contactName, user);
		model.addAttribute("contacts", contactList);
		return "normal/show_contacts";
	}

	// delete contact

	@GetMapping("/{cId}/delete")
	public String deleteContact(@PathVariable("cId") Integer cId, Model model, Principal principal,
			HttpSession session) {

		Contact contact = this.contactRepository.findById(cId).get();
		contact.setUser(null);
		this.contactRepository.delete(contact);

		session.setAttribute("message", new com.smart.helper.Message("Contact Deleted Successfuly", "success"));

		return "redirect:/user/show_customer/0";
	}

	// Update contact Data
	
	

	// process update contact
	

	// View Your Profile
	@GetMapping("/profile")
	public String yourProfile(Model model) {
		model.addAttribute("heading", "View Profile");
		return "normal/profile";
	}
	
	
	
	//For inline editing
	@PostMapping("/updateUserInline")
	public ResponseEntity<String> updateUserInline(@RequestBody Map<String, String> requestBody) {
	    int id = Integer.parseInt(requestBody.get("id"));
	    String field = requestBody.get("field");
	    String value = requestBody.get("value");
	    
	    User user = userRepository.findById(id)
	        .orElseThrow(() -> new IllegalArgumentException("Invalid User id: " + id));

	    if ("name".equals(field)) {
	        user.setName(value);
	    } else if ("about".equals(field)) {
	        user.setAbout(value);
	    } else {
	        return ResponseEntity.badRequest().body("Invalid field: " + field);
	    }

	    userRepository.save(user);
	    return ResponseEntity.ok("User updated successfully");
	}

	
//	@GetMapping("/show_employee/{page}")
//	public String showEmployees(
//	    @PathVariable("page") Integer page,
//	    @RequestParam(name = "employee_name", required = false) String employeeName,
//	    @RequestParam(name = "recordsPerPage", required = false) Integer recordsPerPage,
//	    HttpSession session,  // Add HttpSession to store recordsPerPage per page
//	    Model model,
//	    Principal principal) {
//
//	    model.addAttribute("title", "View Employees");
//	    model.addAttribute("heading", "Employee Details");
//
//	    // Fetch current logged-in user
//	    String userName = principal.getName();
//	    User user = userRepository.getUserByUserName(userName);
//
//	    // Default number of records per page
//	    int defaultRecordsPerPage = 5;
//
//	    // Check if a custom recordsPerPage is provided in the request, save it to the session
//	    if (recordsPerPage != null) {
//	        session.setAttribute("recordsPerPage_" + page, recordsPerPage);
//	    }
//
//	    // Get recordsPerPage from session or use default if none is set
//	    Integer recordsPerPageForPage = (Integer) session.getAttribute("recordsPerPage_" + page);
//	    if (recordsPerPageForPage == null) {
//	        recordsPerPageForPage = defaultRecordsPerPage;  // Use default if not set
//	    }
//
//	    // Handle pagination with recordsPerPage
//	    Pageable pageable = PageRequest.of(page, recordsPerPageForPage);
//	    Page<Addemployee> employees;
//
//	    if (employeeName != null && !employeeName.isEmpty()) {
//	        employees = addemployeeRepository.findByNameContainAndUser(employeeName, user, pageable);
//	    } else {
//	        employees = addemployeeRepository.findEmployeeByUser(user.getId(), pageable);
//	    }
//
//	    // Ensure the page number is valid
//	    if (page < 0 || page >= employees.getTotalPages()) {
//	        page = 0;  // Default to the first page if out of range
//	    }
//
//	    // Pass data to the model
//	    model.addAttribute("employees", employees.getContent());
//	    model.addAttribute("currentPage", page);
//	    model.addAttribute("totalPages", employees.getTotalPages());
//	    model.addAttribute("recordsPerPage", recordsPerPageForPage);  // Add the specific records per page
//
//	    return "normal/show_employee";  // Return the Thymeleaf view
//	}
	


	@GetMapping("/show_employee/{page}")
	public String showEmployees(
	    @PathVariable("page") Integer page,
	    @RequestParam(name = "employee_name", required = false) String employeeName,
	    @RequestParam(name = "recordsPerPage", required = false) Integer recordsPerPage,
	    @RequestParam(name = "department", required = false) String department,
	    @RequestParam(name = "sort", required = false) String sortBy,
	    @RequestParam(name = "direction", defaultValue = "asc") String direction,
	    HttpSession session,
	    Model model,
	    Principal principal) {

	    model.addAttribute("title", "View Employees");
	    model.addAttribute("heading", "Employee Details");

	    String userName = principal.getName();
	    User user = userRepository.getUserByUserName(userName);

	    int defaultRecordsPerPage = 5;
	    recordsPerPage = (recordsPerPage != null) ? recordsPerPage : defaultRecordsPerPage;

	    // Create pageable object with sorting
	    Sort sort = Sort.unsorted();
	    if ("name".equals(sortBy)) {
	        sort = Sort.by(direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, "name");
	    } else if ("phone".equals(sortBy)) {
	        sort = Sort.by(direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, "mobileno");
	    } else if ("department".equals(sortBy)) {
	        sort = Sort.by(direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, "department");
	    }

	    Pageable pageable = PageRequest.of(page, recordsPerPage, sort);

	    Page<Addemployee> employees;
	    if (employeeName != null && !employeeName.isEmpty()) {
	        employees = department != null && !department.isEmpty()
	            ? addemployeeRepository.findByUserAndDepartmentAndNameContaining(user, department, employeeName, pageable)
	            : addemployeeRepository.findByUserAndNameContaining(user, employeeName, pageable);
	    } else {
	        employees = department != null && !department.isEmpty()
	            ? addemployeeRepository.findByUserAndDepartment(user, department, pageable)
	            : addemployeeRepository.findEmployeeByUser(user.getId(), pageable);
	    }

	    model.addAttribute("employees", employees.getContent());
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", employees.getTotalPages());
	    model.addAttribute("recordsPerPage", recordsPerPage);
	    model.addAttribute("selectedDepartment", department);
	    model.addAttribute("sort", sortBy);  // Store current sort field
	    model.addAttribute("sortDirection", direction);  // Store current sort direction

	    // Set the new sort direction for the next click
	    model.addAttribute("nextSortDirection", direction.equals("asc") ? "desc" : "asc");

	    return "normal/show_employee";
	}

	
//	
	// delete employee
	@GetMapping("/{employeeid}/deleteemp")
	public String deleteEmployee(@PathVariable("employeeid") Addemployee employeeid) {
		addemployeeRepository.delete(employeeid);
		return "redirect:/user/show_employee/0";
	}

	// show specific employee by name
	@RequestMapping("/search_employee_name")
	public String listofEmployeeByName(Model model, Principal principal,
			@RequestParam("employee_name") String employeeName) {
		String username = principal.getName();
		User user = userRepository.getUserByUserName(username);

		List<Addemployee> employeeList = addemployeeRepository.findByNameContainAndUser(employeeName, user);
		model.addAttribute("employees", employeeList);

		return "normal/show_employee";
	}
	
	

	

	@GetMapping("/addemployee")
	public String addemployee1(Model model) {
		model.addAttribute("title", "About");
		model.addAttribute("employee", new Addemployee());
		model.addAttribute("heading", "Add Employee ");
		return "normal/addemployeeform";
	}

	@PostMapping("/process_addemployee")
	public String addemployee_process(@ModelAttribute Addemployee addemployee, Principal principal, HttpSession session,
			Model model) {
		String name = principal.getName();
		User user = userRepository.getUserByUserName(name);

		addemployee.setUser(user);
		user.getAddemployee().add(addemployee);
		userRepository.save(user);

		try {

			boolean checkDuplicate = false;
			List<Addemployee> addemployee1 = addemployeeRepository.findAll();

			for (Addemployee add : addemployee1) {
				if (add.getEmail() == null) {
					System.out.println("this is null email");
				}
				if (add.getEmail().equals(addemployee.getEmail())) {
					model.addAttribute("emailerror", "duplicate email id");
					checkDuplicate = false;
					return "normal/addemployeeform";

				} else {
					checkDuplicate = true;
				}

			}

			if (checkDuplicate) {
				addemployeeRepository.save(addemployee);

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return "normal/addemployeeform";
	}

	// delete emp

	@GetMapping("/{Id}/delete")
	public String deleteEmp(@PathVariable("Id") Integer cId, Model model, Principal principal, HttpSession session) {

		Addemployee addemploye = this.addemployeeRepository.findById(cId).get();
		addemploye.setUser(null);
		this.addemployeeRepository.delete(addemploye);

		session.setAttribute("message", new com.smart.helper.Message("Employee Deleted Successfuly", "success"));

		return "redirect:/user/show_employe/0";
	}

	// show student
	@GetMapping("/show_student/{page}")
	public String showStudent(@PathVariable("page") Integer page,
			@RequestParam(name = "studentName", required = false) String studentName,
			@RequestParam(name = "pagenumber", defaultValue = "4") int pagenumber, Model model, Principal principal) {

		model.addAttribute("title", "View Students");
		model.addAttribute("heading", "Student Details ");
		User user = userRepository.getUserByUserName(principal.getName());
		Pageable pageable = PageRequest.of(page, pagenumber);

		Page<Addstudent> students;
		if (studentName != null) {
			students = addstudentRepository.findByNameContainingAndUser(studentName, user, pageable);
		} else {
			students = addstudentRepository.findStudentByUser(user.getId(), pageable);
		}

		// Page<Addstudent> students=null;
//		System.out.println(addstudentRepository.findAll());
		List<Addstudent> student = addstudentRepository.findAll();

		model.addAttribute("students", students.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("pagenumber", pagenumber);
		model.addAttribute("totalPages", students.getTotalPages());

		return "normal/show_student";
	}

	@GetMapping("/show_students/{page}")
	public String showStudents(@PathVariable("page") Integer page, @RequestParam(name = "address") String address,
			Model model, Principal principal) {

		model.addAttribute("title", "View Students");
		model.addAttribute("heading", "Student Details ");
		User user = userRepository.getUserByUserName(principal.getName());
		Pageable pageable = PageRequest.of(page, 2);

		Page<Addstudent> students;
		if (address != null) {
			students = addstudentRepository.searchByAddressContaining(address, pageable);
		} else {
			students = addstudentRepository.findStudentByUser(user.getId(), pageable);
		}

		System.out.println("");
		// Page<Addstudent> students=null;
//		System.out.println(addstudentRepository.findAll());
		List<Addstudent> student = addstudentRepository.findAll();

		model.addAttribute("students", students.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", students.getTotalPages());

		return "normal/show_student";
	}

	// show specific student
	@RequestMapping("/{studentId}/student")
	public String showPerticularStudent(@PathVariable("studentId") Integer studentId, Model model,
			Principal principal) {
		Optional<Addstudent> studentOptional = addstudentRepository.findById(studentId);
		Addstudent student = studentOptional.get();
		String username = principal.getName();
		User user = userRepository.getUserByUserName(username);
		System.out.println("student======" + user.getEmail());
		if (user.getId() == student.getUser().getId()) {
			model.addAttribute("student", student);
			System.out.println(student);
		}
		return "/normal/student_detail";
	}

	// update student
	@RequestMapping("/{studentid}/edit")
	public String showUpdateForm(@PathVariable Integer studentid, Model model) {
		Addstudent student = addstudentRepository.findById(studentid)
				.orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + studentid));
		model.addAttribute("student", student);
		model.addAttribute("heading", "Update Student");
		return "/normal/update_stud";
	}

	@PostMapping("/update_student")
	public String updateStuden(@ModelAttribute Addstudent newStudent, Principal princpal) {
		// Addstudent oldStudent = addstudentRepository.findById(studentid)
		// .orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" +
		// studentid));

		User user = userRepository.getUserByUserName(princpal.getName());
		newStudent.setUser(user);
		user.getStudent().add(newStudent);

		userRepository.save(user);
//		  oldStudent.setName(newStudent.getName());
//		  oldStudent.setEmail(newStudent.getEmail());
//		  oldStudent.setAddress(newStudent.getAddress());
//		  oldStudent.setMobileno(newStudent.getMobileno());

		return "redirect:/user/show_student/0";

	}

	// delete student
	@GetMapping("/{studentid}/deleteStud")
	public String deleteStudent(@PathVariable("studentid") Addstudent studentid) {
		addstudentRepository.delete(studentid);
		return "redirect:/user/show_student/0";
	}

	@GetMapping("/addstudent")
	public String addstudent1(Model model) {
		model.addAttribute("title", "About");
		model.addAttribute("addstudent", new Addstudent());
		model.addAttribute("heading", "Add Student ");

		return "normal/addstudent";
	}

	@RequestMapping("/process_addstudent1")
	public String addstudent_process(@ModelAttribute Addstudent addstudent, Principal principal, HttpSession session,
			Model model) {

		String originalTimestamp = addstudent.getDateandtime();
		System.out.println("timeanddate=" + addstudent.getDateandtime());
		LocalDateTime dateTime = LocalDateTime.parse(originalTimestamp, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a ");
		String formattedTimestamp = dateTime.format(customFormatter);
		System.out.println("Original timestamp: " + originalTimestamp);
		System.out.println("Formatted timestamp: " + formattedTimestamp);

		String name = principal.getName();
		User user = userRepository.getUserByUserName(name);

		addstudent.setUser(user);
		user.getStudent().add(addstudent);
		userRepository.save(user);

		addstudent.setDateandtime(formattedTimestamp);

		System.out.println("checking == " + addstudent.getEmail());

		try {
			boolean checkDuplicate = false;
			List<Addstudent> addstudent1 = addstudentRepository.findAll(); // Fetch existing students
			if (addstudent1 != null) {
				System.out.println("checking the list is null");
			}

			for (Addstudent add : addstudent1) {
				if (add.getEmail() == null) {
					checkDuplicate = false;
				} else if (add.getEmail().equals(addstudent.getEmail())) {
					model.addAttribute("emailerror", "duplicate email id");
					System.out.println("Email ID '" + addstudent.getEmail() + " already exists!");
					System.out.println("Try another email address");
					checkDuplicate = false;
					return "normal/addstudent";
				} else {
					checkDuplicate = true;
				}
			}

			if (checkDuplicate) {

				addstudent.setUser(user);
				user.getStudent().add(addstudent);
				addstudentRepository.save(addstudent);
			}

		} catch (Exception e) {
			System.out.println("Ok");
			System.err.println(addstudent.getName());
			e.printStackTrace();
		}

		return "normal/addstudent";
	}

	@GetMapping("/changePassword")
	public String openSetting(Model model) {
		model.addAttribute("heading", "Change PassWord ");
		return "normal/ChangePassword";
	}

	// change password
	@GetMapping("/changePasswordProcess")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("NewPassword") String NewPassword, Principal principal, HttpSession session, Model model) {
		String userName = principal.getName(); // this pass the login user email id
		User user = userRepository.getUserByUserName(userName);
		System.err.println("oldPassword" + oldPassword);
		System.err.println("NewPassword" + NewPassword);

		System.out.println(user.getPassword());
		if (bcPasswordEncoder.matches(oldPassword, user.getPassword())) {

			user.setPassword(bcPasswordEncoder.encode(NewPassword));
			userRepository.save(user);
			System.err.println("enter correct password");
			session.setAttribute("message", new com.smart.helper.Message("Password Changed Successfuly", "success"));
			return "redirect:/user/index";

		} else {
			session.setAttribute("message", new com.smart.helper.Message("Password Not Match", "danger"));
			model.addAttribute("changeError", true);
			System.err.println("enter wrong password");
			return "normal/ChangePassword";
		}

	}

	// get map loaction

	@GetMapping("/state/{id}")
	public ResponseEntity<?> addState(@PathVariable("id") Integer id, Model model) {
		System.err.println("country id:" + id);

		List<States> state = stateRepository.getAllState(id);
		return ResponseEntity.ok(state);
	}

	@GetMapping("/city/{id}")
	public ResponseEntity<?> addCity(@PathVariable("id") Integer id, Model model) {

		List<Cities> city = cityRepository.getAllCity(id);
		return ResponseEntity.ok(city);
	}

	// get map loacation

	@Autowired
	private GPSRepository gpsRepository;

	@RequestMapping("/getMap")
	public String getMap(Model model) {
		List<GPS> locations = gpsRepository.findAll();
		model.addAttribute("heading", "Map ");
		model.addAttribute("locations", locations);
		return "normal/map";
	}

	// add customer

	@GetMapping("/addcustomer")
	public String addcustomer1(Model model) {
		model.addAttribute("title", "About");
		model.addAttribute("cutsomer", new Addcustomer());
		model.addAttribute("heading", "Add Customer");
		return "normal/addecustomerform";
	}

//Update customer Data
	@PostMapping("/{id}/update_customer")
	public String customerUpdate(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("title", "Update Customer");
		Addcustomer addcustomer = addcustomerRepository.findById(id).get();
		model.addAttribute("customer", addcustomer);
		return "normal/update_customer";
	}

	// update employee
	@RequestMapping("/{id}/update_employee")
	public String employeeUpdate(@PathVariable("id") Integer id, Model model) {
		model.addAttribute("title", "Update employee");
		Addemployee addemploye = addemployeeRepository.findById(id).get();
		model.addAttribute("addemploye ", addemploye);
		return "normal/update_employee";
	}

	@PostMapping("/process_employee")
	public String updateCustomer(@ModelAttribute Addemployee addemployee, Principal principal, HttpSession session) {
		try {
			String name = principal.getName();
			User user = userRepository.getUserByUserName(name);

			addemployee.setUser(user);
			user.getAddemployee().add(addemployee);
			userRepository.save(user);
			System.err.println("user Data " + addemployee);
			session.setAttribute("message", new com.smart.helper.Message("Your employee added", "success"));
		} catch (Exception e) {
			e.printStackTrace();

			session.setAttribute("message", new com.smart.helper.Message("something went wrong", "danger"));
		}
		return "redirect:/user/update_employee/0";
	}

	// update customer
	@RequestMapping("/{customer}/id")
	public String showUpdateCustForm(@PathVariable Integer id, Model model) {
		Addcustomer customer = addcustomerRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + studentid));
		model.addAttribute("customer", customer);
		model.addAttribute("heading", "Update Customer");
		return "/normal/update_customer";
	}

	@PostMapping("/process_updateCustomer")
	public String updateCust(@ModelAttribute Addcustomer addcustomer, Principal principal, HttpSession session) {
		try {
			String name = principal.getName();
			User user = userRepository.getUserByUserName(name);

			addcustomer.setUser(user);
			user.getAddecustomer().add(addcustomer);
			userRepository.save(user);
			System.err.println("user Data " + addcustomer);
			session.setAttribute("message", new com.smart.helper.Message("Your customer added", "success"));
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new com.smart.helper.Message("something went wrong", "danger"));
		}
		return "redirect:/user/show_customer/0";
	}

	// delete customer
	@GetMapping("/{customerid}/deletecust")
	public String cust_del(@PathVariable("customerid") Integer customerid) {
		addcustomerRepository.deleteById(customerid);
		return "redirect:/user/show_customer/0";
	}

//update customer
	@RequestMapping("/{employee}/id")
	public String showUpdateEmpForm(@PathVariable Integer id, Model model) {
		Addemployee addemploye = addemployeeRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + studentid));
		model.addAttribute("addemploye", addemploye);
		model.addAttribute("heading", "Update addemploye");
		return "/normal/update_employee";
	}

	@PostMapping("/process_updateemp")
	public String emp_process(@ModelAttribute Addemployee emp, Principal principal, HttpSession session) {
		try {
			String name = principal.getName();
			User user = userRepository.getUserByUserName(name);

			emp.setUser(user);
			user.getAddemployee().add(emp);
			userRepository.save(user);
			System.err.println("user Data " + emp);
			session.setAttribute("message", new com.smart.helper.Message("Your Contact added", "success"));
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new com.smart.helper.Message("something went wrong", "danger"));
		}
		return "normal/addemployeeform";
	}

	
	@PostMapping("/process_addcoworker")

	public String addcoworker_process(@ModelAttribute Coworker coworker,

			Principal principal,HttpSession session,Model model)

	{

		

		System.out.println("dropdownValue==========================================="

				+ "**********************************************************"+coworker.getDropdownvalue());

		

		    String name=principal.getName();

		    

		    User user=userRepository.getUserByUserName(name);

		    

		    coworker.setUser(user);

		    user.getCoworkers().add(coworker);

		    userRepository.save(user);

		    

		    try {

				boolean checkDuplicate =false;

				List<Coworker> coworkers=addcoworkerRepository.findAll();

				

				for(Coworker co : coworkers )

				{

					if(co.getEmail()==null)

					{

						System.out.println("this is null Email");

					}

					

					if(co.getEmail().equals(coworker.getEmail()))

					{

						model.addAttribute("emailerror", "duplplicate email Id");

						checkDuplicate=false;

						return "normal/addcoworkerform";

					}

					else

					{

						checkDuplicate=true;

					}

				}



				 

		} catch (Exception e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}



		return "normal/addcoworkerform";

	}

	@GetMapping("/addcoworker")
	public String addcoworker(Model model) {
		model.addAttribute("Title", "Add Coworker");
		model.addAttribute("coworker", new Coworker());
		model.addAttribute("heading", "Add Coworker");

		return "normal/addcoworkerform";
	}


	
	  @GetMapping("/show_coworker/{page}")
	  public String showCoworker(@PathVariable ("page") Integer page,
			  @RequestParam(name = "name" ,required = false) String name,
			  @RequestParam(name="dropdownvalue",required = false) String dropdownvalue,
			  @RequestParam(name = "pagelimit" ,defaultValue = "4") int pagelimit,
			 Model model,
			  Principal principal)
	  {
		  model.addAttribute("title","View Coworker");
		  model.addAttribute("heading" ,"Coworker Details");
		  User user = userRepository.getUserByUserName(principal.getName());
		  Pageable pageable  = PageRequest.of(page,pagelimit);
		
		  Page<Coworker> cowPage;
		  
		  if (name != null && dropdownvalue != null) {
		        
		        System.out.println("Name=" + name);
		        cowPage = addcoworkerRepository.findByNameContainingAndDropdownvalueContainingAndUser(name, dropdownvalue, user, pageable);
		    } else if (name != null) {
		      
		        System.out.println("Name=" + name);
		        cowPage = addcoworkerRepository.findByNameContainingAndUser(name, user, pageable);
		    } else if (dropdownvalue != null) {
		       
		        cowPage = addcoworkerRepository.findByDropdownvalueContainingAndUser(dropdownvalue, user, pageable);
		    } else {
		      
		        cowPage = addcoworkerRepository.findCoworkerByUser(user.getId(), pageable);
		    }
		  List<Coworker> coworker= addcoworkerRepository.findAll();
		  
		  model.addAttribute("coworker",cowPage.getContent());
		  model.addAttribute("pagelimit",pagelimit);
		  model.addAttribute("currentPage",page);
		  model.addAttribute("totalPages",cowPage.getTotalPages());
		
		  return "normal/show_coworker";
	  }
	  


	
	
 @RequestMapping("/{coworkerid}/process_updatecoworker")
	  public String updateCoworkerGo(@PathVariable Integer coworkerid,Model model)
	  {
		  Coworker coworker=addcoworkerRepository.findById(coworkerid).orElseThrow(() -> new IllegalArgumentException("Invalid Coworker Id:" + coworkerid));
		  model.addAttribute("coworker",coworker);
		  System.out.println(":==============="+coworker);
		  model.addAttribute("heading","Update Co-worker");
		  return "normal/update_coworker";
	  }
	  
	  @PostMapping("/process_updatecoworker")
	  public String coworkerUpdateProces(@ModelAttribute Coworker coworker,
			  Principal principal,HttpSession session)  
	  {
		  System.out.println("*****************************"+coworker);
		  try {
			String name=principal.getName();
			  User user =userRepository.getUserByUserName(name);
			  
			  coworker.setUser(user);
			  user.getCoworkers().add(coworker);
			  userRepository.save(user);
			  session.setAttribute("message", new com.smart.helper.Message("Your Contact added", "success"));


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			session.setAttribute("message", new com.smart.helper.Message("Something Went Wrong", "danger"));

		}

		return "redirect:/user/show_coworker/0";
	}

	@GetMapping("/{coworkerid}/deletcoworker")
	public String deletecoworker(@PathVariable("coworkerid") Integer coworkerid, Model model, Principal principal,
			HttpSession session) {

		Coworker coworker = this.addcoworkerRepository.findById(coworkerid).get();
		coworker.setUser(null);
		this.addcoworkerRepository.delete(coworker);
		session.setAttribute("message", new com.smart.helper.Message("Employee Deleted Successfully", "success"));
		return "redirect:/user/show_coworker/0";
	}

	@RequestMapping("/{Id}/coworker")
	public String showPerticularCoworker(@PathVariable("Id") Integer Id, Model model, Principal principal) {

		Optional<Coworker> findById = addcoworkerRepository.findById(Id);
		Coworker coworker = findById.get();
		String username = principal.getName();
		User user = userRepository.getUserByUserName(username);
		if (user.getId() == coworker.getUser().getId()) {
			model.addAttribute("coworker", coworker);
		}
		return "normal/coworker_detail";
	}

/////VENDORS METHODS ///////


	// method to add vendor
	@GetMapping("/addvendor")
	public String addvendor(Model model) {
		model.addAttribute("title", "About");
		model.addAttribute("addvendor", new Vendor());
		model.addAttribute("heading", "Add Vendor ");

		return "normal/addvendor";
	}

	@RequestMapping("/process_addvendor")
	public ResponseEntity<Map<String, String>> addvendor_process(@ModelAttribute Vendor addvendor, Principal principal,
			HttpSession session, Model model) {
		Map<String, String> response = new HashMap<>();

		try {
			String originalTimestamp = addvendor.getDateOfRegistration();
			if (originalTimestamp == null || originalTimestamp.isEmpty()) {
				response.put("success", "false");
				response.put("message", "Original timestamp is null or empty.");
				return ResponseEntity.badRequest().body(response);
			}

			LocalDateTime dateTime = LocalDateTime.parse(originalTimestamp, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
			DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");
			String formattedTimestamp = dateTime.format(customFormatter);

			String name = principal.getName();
			User user = userRepository.getUserByUserName(name);

			addvendor.setUser(user);
			addvendor.setDateOfRegistration(formattedTimestamp);

			boolean isDuplicate = vendorRepository.existsByEmail(addvendor.getEmail());
			if (isDuplicate) {
				response.put("success", "false");
				response.put("message", "Duplicate email id");
				return ResponseEntity.badRequest().body(response);
			}

			vendorRepository.save(addvendor);

			response.put("success", "true");
			response.put("message", "Vendor added successfully.");
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			System.out.println("Exception occurred during vendor addition process");
			e.printStackTrace();
			response.put("success", "false");
			response.put("message", "Exception occurred during vendor addition process");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	// show vendor
	@GetMapping("/show_vendor/{page}")
	public String showVendor(@PathVariable("page") Integer page,
			@RequestParam(name = "vendor_name", required = false) String vendorName, Model model, Principal principal) {
		model.addAttribute("title", "View Vendors");
		model.addAttribute("heading", "Vendor details");

		User user = userRepository.getUserByUserName(principal.getName());

		if (page == null || page < 0) {
			page = 0;
		}

		Pageable pageable = PageRequest.of(page, 5);

		Page<Vendor> vendors;
		if (vendorName != null && !vendorName.isEmpty()) {
			vendors = vendorRepository.findByNameContainAndUser(vendorName, user, pageable);
		} else {
			vendors = vendorRepository.findVendorByUser(user.getId(), pageable);
		}

		model.addAttribute("vendor", vendors.getContent());
		model.addAttribute("currentPage", vendors.getNumber() + 1);
		model.addAttribute("totalPages", vendors.getTotalPages());

		return "normal/show_vendor";
	}

	// show specific vendors
	@RequestMapping("/{vendorId}/vendor")
	public String showParticularVendor(@PathVariable("vendorId") Integer vendorId, Model model, Principal principal) {
		Optional<Vendor> vendorOptional = vendorRepository.findById(vendorId);
		Vendor vendor = vendorOptional.get();
		String username = principal.getName();
		User user = userRepository.getUserByUserName(username);
		System.out.println("student === " + user.getEmail());
		if (user.getId() == vendor.getUser().getId()) {
			model.addAttribute("vendor", vendor);
			System.out.println(vendor);
			System.out.println("in");
		}
		return "normal/vendor_detail";
	}

	@RequestMapping("/{vendorId}/editVendor")
	public String showUpdateVendor(@PathVariable Integer vendorId, Model model) {
		Vendor vendor = vendorRepository.findById(vendorId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Vendor: " + vendorId));
		model.addAttribute("vendor", vendor);
		model.addAttribute("heading", "Update Vendor");
		return "/normal/update_vendor";
	}

	// edit
	@PostMapping("/update_vendor")
	public String updateVendor(@ModelAttribute Vendor newVendor, Principal principal) {
		User user = userRepository.getUserByUserName(principal.getName());
		newVendor.setUser(user);
		user.getVendors().add(newVendor);

		userRepository.save(user);

		return "redirect:/user/show_vendor/0";
	}

	// delete
	@GetMapping("/{vendorid}/deleteVendor")
	public String deleteVendor(@PathVariable("vendorid") Vendor vendorid) {
		vendorRepository.delete(vendorid);
		return "redirect:/user/show_vendor/0";
	}

	// College functinality apis


	@GetMapping("/addCollege")
	public String addCollege(Model model) {
		model.addAttribute("college", new College());
		model.addAttribute("heading", "Add College");

		return "normal/add_college";
	}

	@PostMapping("/addCollege")
	public String addCollege(@ModelAttribute("college") College college, Principal principal, HttpSession session,
			Model model) {

		String name = principal.getName();
		User user = userRepository.getUserByUserName(name);

		if (collegeService.isEmailExists(college.getEmail())) {
			model.addAttribute("emailExistsError", "Email already exists");
			return "normal/add_college";
		}

		College c = new College();
		c.setAddress(college.getAddress());
		c.setContactNumber(college.getContactNumber());
		c.setEmail(college.getEmail());
		c.setName(college.getName());
		c.setUser(user);
		System.out.println(user);
		collegeService.addCollege(c);
		return "normal/add_college"; // Redirect to the add college form page after adding the college
	}

	@GetMapping("/colleges/data")
	@ResponseBody
	public Page<College> getCollegesData(@RequestParam(defaultValue = "0") int page, Model model) {
		int pageSize = 5; // Number of items per page
		return collegeService.findPaginated(page, pageSize);
	}

	@GetMapping("/deleteCollege/{id}")
	public String deleteCollege(@PathVariable("id") Integer id) {
		collegeService.deleteCollege(id);
		// return "redirect:/user/colleges"; // Redirect to the colleges list page after
		// deleting
		return "redirect:/user/new/colleges";
	}

	@GetMapping("/editCollege/{id}")
	public String editCollegeForm(@PathVariable("id") Integer id, Model model) {
		College college = collegeService.getCollegeById(id);
		model.addAttribute("college", college);
		model.addAttribute("heading", "Edit College");
		return "normal/edit_college"; // Return the HTML template for editing a college
	}

	@PostMapping("/editCollege/{id}")
	public String editCollege(@PathVariable("id") Integer id, @ModelAttribute("college") College college,
			Principal principal) {
		User user = userRepository.getUserByUserName(principal.getName());
		college.setUser(user);
		System.out.println("in edit collegge");
		collegeService.updateCollege(college);
		// return "redirect:/user/colleges"; // Redirect to the colleges list page after
		// updating
		return "normal/collegescolumn";
	}

	@GetMapping("/getAllColleges")
	@ResponseBody
	public Page<College> getColleges(@RequestParam(name = "page", defaultValue = "0") int page) {
		// Implement your logic to fetch colleges for a specific page
		Page<College> collegesPage = collegeService.getColleges(page);
		return collegesPage;
	}

	@GetMapping("/colleges")
	public String showColleges(Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5", name = "pageSize") int pageSize,
			@RequestParam(required = false, name = "college_name") String searchTerm) {

		Page<College> collegePage;
		List<College> colleges;
		int totalPages;
		System.out.println(searchTerm);

		if (searchTerm == null) {
			model.addAttribute("searchTerm", "");
		} else {
			model.addAttribute("searchTerm", searchTerm);

		}

		if (searchTerm != null && !searchTerm.isEmpty()) {
			// If a search term is provided, search for colleges using the term
			collegePage = collegeService.searchCollegeByAnyFieldPaginated(searchTerm, page, pageSize);
			System.out.println(collegePage);
		} else {
			System.out.println("in else loop");
			System.out.println(page);
			System.out.println(pageSize);
			collegePage = collegeService.findPaginated(page, pageSize);
			System.out.println(collegePage);
		}

		colleges = collegePage.getContent();
		totalPages = collegePage.getTotalPages();
		System.out.println("total pages:" + totalPages);

		System.out.println(colleges);

		model.addAttribute("colleges", colleges);
		model.addAttribute("currentPage", page);
		model.addAttribute("pageSize", pageSize);

		model.addAttribute("totalPages", totalPages);
		model.addAttribute("heading", "College details");
		model.addAttribute("startPage", Math.max(1, page - 1));
		model.addAttribute("endPage", Math.min(totalPages, page + 1));

		return "normal/colleges"; // Return the HTML template for displaying colleges

	}

	@GetMapping("/searchCollege")
	public ResponseEntity<List<College>> searchColleges(Model model, @RequestParam("college_name") String searchTerm,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5", name = "pageSize") int pageSize) {

		// List<College> colleges = collegeService.searchCollegeByAnyField(searchTerm);

		Page<College> collegePage = collegeService.searchCollegeByAnyFieldPaginated(searchTerm, page, pageSize);
		List<College> colleges = collegePage.getContent();
		int totalPages = collegePage.getTotalPages();
		System.out.println("totalPages :" + totalPages);
		System.out.println(pageSize);

		if (searchTerm == null) {
			model.addAttribute("searchTerm", "");
		} else {
			model.addAttribute("searchTerm", searchTerm);

		}

		model.addAttribute("colleges", collegePage);
		model.addAttribute("currentPage", page);
		model.addAttribute("pageSize", pageSize);

		model.addAttribute("totalPages", totalPages);
		model.addAttribute("heading", "College details");
		model.addAttribute("startPage", Math.max(1, page - 1));
		model.addAttribute("endPage", Math.min(totalPages, page + 1));

		model.addAttribute("heading", "Search Results for: " + searchTerm);

		return new ResponseEntity<>(colleges, HttpStatus.OK);
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------------------//

	@GetMapping("/new/addCollege")
	public String showAddCollegeForm(Model model, Principal principal) {

		// get current logged in user's username
		String username = principal.getName();
		User currentUser = userRepository.getUserByUserName(username);

		Map<String, String> renamedColumns = columnMetadataService.getAllRenamedColumnNames(currentUser);
		model.addAttribute("renamedColumns", renamedColumns);
		model.addAttribute("college", new CollegeDto());
		return "normal/addCollege";
	}

//	@PostMapping("/new/addCollege")
//    public String addCollege(@ModelAttribute CollegeDto collegeDTO,Model model,Principal principal) {
//		
//		// get current logged in user's username
//		String username = principal.getName();
//		User currentUser = userRepository.getUserByUserName(username);
//		
//       
//		if (!collegeDTO.getCol6Renamed().isEmpty()) {
//	        columnMetadataService.saveRenamedColumnName("col6", collegeDTO.getCol6Renamed(), currentUser);
//	    }
//	    if (!collegeDTO.getCol7Renamed().isEmpty()) {
//	        columnMetadataService.saveRenamedColumnName("col7", collegeDTO.getCol7Renamed(), currentUser);
//	    }
//        
//        Map<String, String> renamedColumns = columnMetadataService.getAllRenamedColumnNames(currentUser);
//
//        // Convert DTO to entity and save college
//        College college = new College();
//        college.setName(collegeDTO.getName());
//        college.setContactNumber(collegeDTO.getContactNumber());
//        college.setEmail(collegeDTO.getEmail());
//        college.setAddress(collegeDTO.getAddress());
//        college.setCol6(collegeDTO.getCol6());
//        college.setCol7(collegeDTO.getCol7());
//        college.setUser(currentUser);
//        collegeService.saveCollege(college);
//        model.addAttribute("renamedColumns", renamedColumns);
//		model.addAttribute("college", new CollegeDto());
//
//        return "normal/addCollege";
//    }

	@PostMapping("/new/addCollege")
	public String addCollege(@ModelAttribute CollegeDto collegeDTO, Model model, Principal principal) {
		// get current logged in user
		String username = principal.getName();
		User currentUser = userRepository.getUserByUserName(username);

		// Save renamed columns if they are not empty
//	    if (!collegeDTO.getCol6Renamed().isEmpty()) {
//	        columnMetadataService.saveRenamedColumnName("col6", collegeDTO.getCol6Renamed(), currentUser);
//	    } else {
//	       
//	        columnMetadataService.removeRenamedColumnName("col6", currentUser);
//	    }
//	    if (!collegeDTO.getCol7Renamed().isEmpty()) {
//	        columnMetadataService.saveRenamedColumnName("col7", collegeDTO.getCol7Renamed(), currentUser);
//	    } else {
//	        
//	        columnMetadataService.removeRenamedColumnName("col7", currentUser);
//	    }

		Map<String, String> renamedColumns = columnMetadataService.getAllRenamedColumnNames(currentUser);

		// System.out.println(renamedColumns.toString());

		// Convert DTO to entity and save college
		College college = new College();
		college.setName(collegeDTO.getName());
		college.setContactNumber(collegeDTO.getContactNumber());
		college.setEmail(collegeDTO.getEmail());
		college.setAddress(collegeDTO.getAddress());
		college.setCol1(collegeDTO.getCol1());
		college.setCol2(collegeDTO.getCol2());
		college.setCol3(collegeDTO.getCol3());
		college.setCol4(collegeDTO.getCol4());
		college.setCol5(collegeDTO.getCol5());
		college.setUser(currentUser);
		collegeService.saveCollege(college);

		model.addAttribute("renamedColumns", renamedColumns);
		model.addAttribute("college", new CollegeDto());

		return "normal/addCollege";
	}

	@GetMapping("/new/colleges")
	public String getAllColleges(Model model, Principal principal,
			@RequestParam(value = "searchTerm", required = false) String searchTerm,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "pageSize", defaultValue = "5") int size) {

		String username = principal.getName();
		User currentUser = userRepository.getUserByUserName(username);

		System.out.println("pageSize: " + size);
		if (searchTerm == null) {
			model.addAttribute("searchTerm", "");
		} else {
			model.addAttribute("searchTerm", searchTerm);

		}

		// List<College> colleges = collegeService.getAllColleges(currentUser);
		Page<College> collegePage = collegeService.getAllColleges(currentUser, searchTerm, page, size);
		List<College> colleges = collegePage.getContent();
		Map<String, String> renamedColumns = columnMetadataService.getAllRenamedColumnNames(currentUser);

		model.addAttribute("colleges", colleges);
		model.addAttribute("renamedColumns", renamedColumns);
		model.addAttribute("currentPage", page);
		model.addAttribute("pageSize", size);
		model.addAttribute("totalPages", collegePage.getTotalPages());
		model.addAttribute("heading", "College details");
		model.addAttribute("startPage", Math.max(1, page - 1));
		model.addAttribute("endPage", Math.min(collegePage.getTotalPages(), page + 1));

		return "normal/collegescolumn";
	}

	@GetMapping("/new/searchColleges")
	public ResponseEntity<List<College>> searchColleges(Model model, Principal principal,
			@RequestParam(value = "searchTerm", required = false) String searchTerm,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "pageSize", defaultValue = "5") int size) {

		String username = principal.getName();
		User currentUser = userRepository.getUserByUserName(username);

		if (searchTerm == null) {
			model.addAttribute("searchTerm", "");
		} else {
			model.addAttribute("searchTerm", searchTerm);

		}

		// List<College> colleges = collegeService.getAllColleges(currentUser);
		Page<College> collegePage = collegeService.getAllColleges(currentUser, searchTerm, page, size);
		List<College> colleges = collegePage.getContent();
		Map<String, String> renamedColumns = columnMetadataService.getAllRenamedColumnNames(currentUser);

		model.addAttribute("colleges", colleges);
		model.addAttribute("renamedColumns", renamedColumns);
		model.addAttribute("currentPage", page);
		model.addAttribute("pageSize", size);
		model.addAttribute("totalPages", collegePage.getTotalPages());
		model.addAttribute("heading", "College details");
		model.addAttribute("startPage", Math.max(1, page - 1));
		model.addAttribute("endPage", Math.min(collegePage.getTotalPages(), page + 1));

		return new ResponseEntity<>(colleges, HttpStatus.OK);
	}

//    @PostMapping("/new/saverenamedcolumns")
//    public ResponseEntity<?> saveRenamedColumns(@RequestBody List<ColumnMetadataDto> columns,Principal principal,Model model){
//    	
//    	// get current user
//    	String username = principal.getName();
//    	User currentUser = userRepository.getUserByUserName(username);
//    	
//    	columnMetadataService.saveOrUpdateColumns(columns,currentUser);
//    	Map<String, String> renamedColumns = columnMetadataService.getAllRenamedColumnNames(currentUser);
//    	model.addAttribute("renamedColumns", renamedColumns);
//    	
//    	return  ResponseEntity.ok().build();
//    	
//    }

	@PostMapping("/new/saverenamedcolumns")
	public String saveRenamedColumns(@RequestBody List<ColumnMetadataDto> columns, Principal principal, Model model) {

		// get current user
		String username = principal.getName();
		User currentUser = userRepository.getUserByUserName(username);

		columnMetadataService.saveOrUpdateColumns(columns, currentUser);
		// Map<String, String> renamedColumns =
		// columnMetadataService.getAllRenamedColumnNames(currentUser);
		// model.addAttribute("renamedColumns", renamedColumns);

		return "normal/addCollege";

	}

	@GetMapping("/new/getallcolumns")
	public ResponseEntity<Map<String, String>> getAllColumns(Principal principal) {
		String username = principal.getName();
		User currentUser = userRepository.getUserByUserName(username);
		Map<String, String> renamedColumns = columnMetadataService.getAllRenamedColumnNames(currentUser);

		return new ResponseEntity<Map<String, String>>(renamedColumns, HttpStatus.OK);
	}

	@GetMapping("/addSchoolForm")
	public String addSchoolForm(Model model, Principal principal) {

		String username = principal.getName();
		User currentUser = userRepository.getUserByUserName(username);

		model.addAttribute("school", new School());
		Map<String, String> customColumns = schoolService.getAllCustomColumns(currentUser);
		System.out.println("Custom Columns: " + customColumns);
		model.addAttribute("customColumns", customColumns);
		model.addAttribute("heading", "Add School");
		return "normal/addSchool";
	}

	@PostMapping("/addSchool")
	public ResponseEntity<School> addSchool(@RequestBody School school, Principal principal) {

		System.out.println("in add school");
		// get current user
		String username = principal.getName();
		User currentUser = userRepository.getUserByUserName(username);

		System.out.println(school.toString());

		School createdSchool = schoolService.addSchool(school, currentUser);

		return ResponseEntity.status(HttpStatus.CREATED).body(createdSchool);

	}

//    @PostMapping("/addSchool")
//	public String addSchool(@RequestBody School school,Principal principal){
//		
//    	System.out.println("in add school");
//		// get current user
//		String username = principal.getName();
//		User currentUser = userRepository.getUserByUserName(username);
//		
//		System.out.println(school.toString());
//		
//		School createdSchool = schoolService.addSchool(school,currentUser);
//		
//		return "normal/addSchool";
//		
//	}

	@GetMapping("/school/getAllCustomColumns")
	public ResponseEntity<Map<String, String>> getAllCustomColumns(Principal principal) {
		String username = principal.getName();
		User currentUser = userRepository.getUserByUserName(username);

		Map<String, String> customColumns = schoolService.getAllCustomColumns(currentUser);
		return new ResponseEntity<Map<String, String>>(customColumns, HttpStatus.OK);

	}

	@PostMapping("/school/updatecustomcolumns")
	public void updateCustomColumns(@RequestBody CustomColumnsDTO customColumnDTO, Principal principal) {
		// get current user
		String username = principal.getName();
		User currentUser = userRepository.getUserByUserName(username);

		schoolService.updateCustomColumns(customColumnDTO, currentUser);

	}

	@GetMapping("/school/getAll")
	public String getAllSchools(Model model, Principal principal) {
		String username = principal.getName();
		User currentUser = userRepository.getUserByUserName(username);

		List<School> schools = schoolService.getAllSchools(currentUser);
		model.addAttribute("schools", schools);
		return "normal/schoolList";
	}

	@GetMapping("/add")
	public String showAddcompany(Model model) {
		model.addAttribute("Addcompany", new Addcompany());
		return "normal/add_company";
	}
	
	
	

	@GetMapping("/add_company")
	public String addcompany(Model model) {
		model.addAttribute("title", "Add company");
		model.addAttribute("Addcompany", new Addcustomer());
		model.addAttribute("heading", "Add Company");

		return "normal/add_company";
	}
	
	

	@PostMapping("/process_company")
	public String company_process(@ModelAttribute Addcompany addcompany, Principal principal, HttpSession session) {
		try {
			String name = principal.getName();
			User user = userRepository.getUserByUserName(name);

			addcompany.setUser(user);
			user.getAddecompany().add(addcompany);
			userRepository.save(user);
			System.err.println("user Data " + addcompany);
			session.setAttribute("message", new com.smart.helper.Message("Your company added", "success"));
		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new com.smart.helper.Message("something went wrong", "danger"));
		}
		return "normal/add_company";
	}
	
	
	
	
	@GetMapping("/show_company/{page}")
	public String showCompany(@PathVariable(name = "page") int page,
	                          @RequestParam(name = "companyName", required = false) String companyName,
	                          @RequestParam(name = "dropdownvalue", defaultValue = "10") int dropdownValue,
	                          Model model,
	                          Principal principal) {
	    model.addAttribute("title", "View Company");
	    model.addAttribute("heading", "View Company");

	    // Retrieve currently logged-in user
	    String username = principal.getName();
	    User user = userRepository.getUserByUserName(username);

	    // Create Pageable for pagination
	    Pageable pageable = PageRequest.of(page, dropdownValue);

	    Page<Addcompany> addCompanyPage;

	    // Check if companyName is provided for filtering
	    if (companyName != null && !companyName.isEmpty()) {
	        addCompanyPage = addcompanyrepository.findByNameContainingAndUser(companyName, user, pageable);
	    } else {
	        addCompanyPage = addcompanyrepository.findCompanyByUser(user.getId(), pageable);
	    }

	    // Add attributes to the model for rendering in the view
	    model.addAttribute("company", addCompanyPage.getContent());
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", addCompanyPage.getTotalPages());
	    model.addAttribute("dropdownValue", dropdownValue); // To pass back the dropdown value
	    model.addAttribute("message", "Showing companies for page: " + page);

	    return "normal/show_company";
	}

	
		

		
		
		
		@GetMapping("/{companyid}/update_company")
	    public String showUpdateCompanyForm(@PathVariable("companyid") int id, Model model,Principal principal) {
	        
	        Addcompany addcompany = addcompanyrepository.findById(id);
	        String username = principal.getName();
	        User user=userRepository.getUserByUserName(username);
	        if (addcompany !=null && user!=null && addcompany.getUser().equals(user)) {
	            model.addAttribute("addcompany", addcompany);
	        }
	            return "normal/update_company";
	        
	    }
		
		@PostMapping("/process_updateCompany")
		public String updateComp(@ModelAttribute Addcompany addcompany, Principal principal, HttpSession session) {
			try {
				String name = principal.getName();
				User user = userRepository.getUserByUserName(name);

				addcompany.setUser(user);
				user.getAddecompany().add(addcompany);
				userRepository.save(user);
				System.err.println("user Data " + addcompany);
				session.setAttribute("message", new com.smart.helper.Message("Your company added", "success"));
			} catch (Exception e) {
				e.printStackTrace();
				session.setAttribute("message", new com.smart.helper.Message("something went wrong", "danger"));
			}
			return "redirect:/user/show_company/0";
		}

		
		
		@GetMapping("/{companyid}/deleteCompany")
		public String deleteCompany(@PathVariable("companyid") Addcompany companyid) {
			addcompanyrepository.delete(companyid);
			return "redirect:/user/show_company/0";
		}
		
		

		@RequestMapping("/searchCompany/{page}")
		public String listofCompanyByName(Model model, Principal principal,
		        @RequestParam(value="company_name", required=false) String companyName,
		        Pageable pageable) {
		    String username = principal.getName();
		    User user = userRepository.getUserByUserName(username);

		    Page<Addcompany> companyPage = addcompanyrepository.findByNameContainingIgnoreCase(companyName, pageable);
		    model.addAttribute("companyPage", companyPage);
		   

		    return "normal/show_company";
		}

		@GetMapping("/user/show_company/0")
	    @ResponseBody
	    public String showCompany(@RequestParam(name = "pagenumber", required = false) String pageNumber) {
	        if (pageNumber != null && !pageNumber.isEmpty()) {
	            // Process pageNumber as needed (e.g., fetch data from a service)
	            return "Selected page number: " + pageNumber;
	        } else {
	            return "Please select a page number.";
	        }
		}
		
		//////////MECHPRODUCTION///////////////////////
		
		////// Add data ////////
			@Autowired
			private MechProductionService mechProductionService;
			
			@Autowired
			private MechCountryRepository mechCountryRepository;
			
			@Autowired
			private MechStateRepository mechStateRepository;
			
			@Autowired
		    private MechCityRepository mechCityRepoitory;

			@GetMapping("/mech_company")
			public String mechProduction(Model model) {
				model.addAttribute("title", "Add Mech Company");
				model.addAttribute("mechProduction", new MechProduction());
				model.addAttribute("heading", "Add Mech Production");
				List<MechCountry> mechCountries = mechCountryRepository.findAll();
				model.addAttribute("countries", mechCountries);
		        System.out.println("contry data:"+ mechCountries);

				return "normal/mech_company";
			}

			@PostMapping("/mech_process")
			public String mechProduction_process(@ModelAttribute MechProduction mech_company, Principal principal,
					HttpSession session, Model model) {
				try {
					String name = principal.getName();
					User user = userRepository.getUserByUserName(name);
					System.out.println("mech_company added data" + mech_company);
					mech_company.setUser(user);
					user.getMechProduction().add(mech_company);
					userRepository.save(user);
//					mechProductionService.save(mech_company);
					System.err.println("user Data " + mech_company);
					session.setAttribute("message", new com.smart.helper.Message("Your customer added", "success"));
				} catch (Exception e) {
					e.printStackTrace();
					session.setAttribute("message", new com.smart.helper.Message("something went wrong", "danger"));
				}
				
				List<MechCountry> mechCountries = mechCountryRepository.findAll();
				List<MechState> mechStates = mechStateRepository.findAll();
				List<MechCity> mechCities = mechCityRepoitory.findAll();
				model.addAttribute("countries", mechCountries);
				model.addAttribute("states", mechStates);
				model.addAttribute("cities", mechCities);
				return  "normal/mech_company";
			}

			////// retrive all data //////
			@GetMapping("/mechProductionList/{page}")
			public String findAll(@PathVariable("page") Integer page, Principal principal, Model model) {

				String username = principal.getName();
				User user = userRepository.getUserByUserName(username);
				System.out.println("user to pass" + user);
				if (user != null) {
					Page<MechProduction> mechProductionPage = mechProductionService.findAll(page, 3);
					model.addAttribute("mechProductions", mechProductionPage.getContent());
					model.addAttribute("currentPage", page);
					model.addAttribute("totalPages", mechProductionPage.getTotalPages());
				}

				return "normal/mechProductionList";
			}

			@GetMapping("/goToPage")
			public String goToPage(@RequestParam("page") Integer page) {
				System.out.println(page);
				return "redirect:/user/mechProductionList/" + (page - 1);

			}

		///////////delete by id/////////
			@GetMapping("/delete/{id}")
			public String deleteMechProduction(@PathVariable int id, Principal principal, Model model) {
				MechProduction mechProduction = mechProductionService.findById(id);
				String username = principal.getName();
				User user = userRepository.getUserByUserName(username);

				if (mechProduction != null && user != null && mechProduction.getUser().equals(user)) {
					mechProductionService.deleteById(id);
				}

				Page<MechProduction> mechProductionPage = mechProductionService.findAll(0, 2);
				model.addAttribute("mechProductions", mechProductionPage.getContent());
				model.addAttribute("currentPage", 0);
				model.addAttribute("totalPages", mechProductionPage.getTotalPages());
				return "normal/mechProductionList";
			}

		///////////edit by id//////////////////
			@GetMapping("/edit/{id}")
			public String showEditForm(@PathVariable("id") int id, Model model, Principal principal) {
				MechProduction mechProduction = mechProductionService.findById(id);
				String username = principal.getName();
				User user = userRepository.getUserByUserName(username);
				List<MechCountry> mechCountries = mechCountryRepository.findAll();
				List<MechState> mechStates = mechStateRepository.findAll();
				List<MechCity> mechCities = mechCityRepoitory.findAll();
				if (mechProduction != null && user != null && mechProduction.getUser().equals(user)) {
					model.addAttribute("mechProduction", mechProduction);
					
					model.addAttribute("countries", mechCountries);
					model.addAttribute("states", mechStates);
					model.addAttribute("cities", mechCities);
			        System.out.println("contry data in edit:"+ mechCountries);

					return "normal/editMechCompany";
				}

				Page<MechProduction> mechProductionPage = mechProductionService.findAll(0, 2);
				model.addAttribute("mechProductions", mechProductionPage.getContent());
				model.addAttribute("currentPage", 0);
				model.addAttribute("totalPages", mechProductionPage.getTotalPages());
				return "normal/mechProductionList";
			}

			@PostMapping("/update/{id}")
			public String updateMechProduction(@ModelAttribute MechProduction mechProduction, @PathVariable("id") int id,
					Principal principal, Model model) {
				String username = principal.getName();
				User user = userRepository.getUserByUserName(username);
				System.out.println("new  entered data:" + mechProduction);
				if (user != null) {
					// Ensure the correct ID is set before saving
					MechProduction existingMechProduction = mechProductionService.findById(id); // mechProduction.getMechId()
					if (existingMechProduction != null && existingMechProduction.getUser().equals(user)) {
						existingMechProduction.setName(mechProduction.getName());
						existingMechProduction.setMfgDate(mechProduction.getMfgDate());
						existingMechProduction.setModelNo(mechProduction.getModelNo());
						existingMechProduction.setFinalizerName(mechProduction.getFinalizerName());
						existingMechProduction.setCountryName(mechProduction.getCountryName());
						existingMechProduction.setStateName(mechProduction.getStateName());
						existingMechProduction.setCityName(mechProduction.getCityName());
						mechProductionService.save(existingMechProduction);
					}
				}

				Page<MechProduction> mechProductionPage = mechProductionService.findAll(0, 2);
				model.addAttribute("mechProductions", mechProductionPage.getContent());
				model.addAttribute("currentPage", 0);
				model.addAttribute("totalPages", mechProductionPage.getTotalPages());
				return "normal/mechProductionList";
			}

		///////////search by name/////////////
			@RequestMapping("/mechProdList")
			public String searchMechProduction(@RequestParam(value = "name", required = false) String name,
					@RequestParam(defaultValue = "0") int page, Model model) {
				Page<MechProduction> mechProductionPage;
				if (name != null && !name.trim().isEmpty()) {
					mechProductionPage = mechProductionService.findByNameContainingIgnoreCase(name, page, 2);
					if (mechProductionPage.isEmpty()) {
						model.addAttribute("error", "No MechProductions found with the given name.");
					} else {
//						Page<MechProduction> mechProductionPage = mechProductionService.findAll(0, 2);
						model.addAttribute("mechProductions", mechProductionPage.getContent());
						model.addAttribute("currentPage", 0);
						model.addAttribute("totalPages", mechProductionPage.getTotalPages());
						return "normal/mechProductionList";
					}
				} else {
					model.addAttribute("error", "Please enter a valid name.");
				}
				Page<MechProduction> mechProdPage = mechProductionService.findAll(0, 2);
				model.addAttribute("mechProductions", mechProdPage.getContent());
				model.addAttribute("currentPage", 0);
				model.addAttribute("totalPages", mechProdPage.getTotalPages());
				return "normal/mechProductionList";
			}

			//////////////////////////////////////////////////////////////////
			
			
		    @GetMapping("/getState/{countryName}")
		    public ResponseEntity<?> getStatesByCountryId(@PathVariable String countryName) {
		    	System.err.println("country "+countryName);
		    	List<MechState> mechStates=mechStateRepository.findByMechCountry_CountryName(countryName);
		    	System.out.println("mech states data:"+ mechStates);
		        return ResponseEntity.ok(mechStates);
		    }

		    
		    @GetMapping("/getCity/{stateName}")
		    public ResponseEntity<?> getCitiesByStateId(@PathVariable String stateName) {
		    	System.out.println("state id for refer:"+ stateName);
		    	List<MechCity> mechCity=mechCityRepoitory.findByMechState_StateName(stateName);
		    	System.out.println("cities are :"+ mechCity);
		        return ResponseEntity.ok(mechCity);
		    }
		
		
		

		}

	
