# ASDE Group 3 Project
- Carvelli Gennaro Walter
- De Marco Cristian
- Fico Domenico
- Oliverio Nuccia
- Pace Giuseppe

## Import PostgreSQL test database

Follow this guide using pgAdmin 4:
1. Create new database called "SignMe"
2. Right click on SignMe>Schemas>Public and select "Drop Cascade"
3. Right click on SignMe database and select "Restore..."
4. Select file "*.backup" in our repo and "postgres" as Role
5. Click "Restore"
6. Done

In case of problems follow this [link](https://o7planning.org/en/11913/backup-and-restore-postgres-database-with-pgadmin)

## Structure of React Components

  
### Authentication Page
 - LoginHandler

### Home Page
 - Home
 - CourseCreation
 - TabPanel
	 - CreatedCoursesList
	 - SubscribedCoursesList
 
### Course Page
 - CoursePage
 - LectureItems
	 - FabGroup *(includes creation of lectures, notices and material upload)*
	 - ManualAddition
 - NoticesList
 - MaterialList
 - StudentSubscriptions

### Style
 - LoginRegisterStyle
 - HomeStyle
 - TabsStyle
