import React from 'react';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import { useStyles } from './HomeStyle.js';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import Divider from '@material-ui/core/Divider';
import { createBrowserHistory } from 'history';
import Cookies from 'js-cookie'
import {Link} from 'react-router-dom'
import BaseInstance from '../http-client/BaseInstance';

export default function CoursesList() {
    const classes = useStyles();
    var courses = JSON.parse(Cookies.get("subscribedCourses"));
    const [count, setCount] = React.useState(0)
    const history = createBrowserHistory()
    
    const getAttendancesNumber = (courseId) => {
        BaseInstance.get("getAttendancesNumber", {params:{email:Cookies.get("email"), courseId:courseId}}).then ( res => {
            console.log(res.data)
            setCount(res.data)
        }) 
        return count
    }

    const goToCourse = course => {
        Cookies.set("currentCourse", course)

        var courseId = course.courseId
        BaseInstance.get("getCourseInfo", {params: {courseId: courseId}}).then(res => {
            var lectures = res.data.lecturesInfoDTO.lectures
            var lecturerId = res.data.lecturesInfoDTO.lecturer
            var students = res.data.users
            var notices = res.data.notices
            var material = res.data.material

            lectures.sort((a, b) => (a.date > b.date) ? 1 : -1)
            students.sort((a,b) => (a.lastName > b.lastName)? 1 : -1)
            notices.sort((a,b) => (a.noticeId > b.noticeId)? 1 : -1)
            material.sort((a,b) => (a.materialId > b.materialId)? 1 : -1)

            Cookies.set("currentLectures", lectures)
            Cookies.set("lecturerId", lecturerId);
            Cookies.set("currentStudents", students)
            Cookies.set("courseNotices", notices)
            Cookies.set("material", material)
                
            history.push("/course");
            window.location.reload()
        })


    }

    return (
        <List component="nav">
            <ListItem className={classes.courseItem} item xs={12}>
                <Typography className={classes.createdCourseNameHeader}>Courses</Typography>
                {/* <Typography className={classes.courseAttendaceHeader}>Attendences</Typography> */}
            </ListItem>
            <Divider></Divider>
            <div className={classes.coursesContainer}>
                {courses.map((course) => (
                    <div key={course.courseId}>
                        <ListItem className={classes.courseItem} xs={12} >
                            <ListItem button>
                               <ListItemText className={classes.courseName} onClick={() => goToCourse(course)}>{course.subject}</ListItemText>
                            </ListItem>
                            {/*<Typography className={classes.courseAttendace}>{count}</Typography>*/}
                        </ListItem>
                        <Divider></Divider>
                    </div>
                ))}
            </div>
        </List>
    )
};