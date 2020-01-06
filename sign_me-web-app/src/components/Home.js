import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import clsx from 'clsx';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardMedia from '@material-ui/core/CardMedia';
import CardContent from '@material-ui/core/CardContent';
import CardActions from '@material-ui/core/CardActions';
import Collapse from '@material-ui/core/Collapse';
import Avatar from '@material-ui/core/Avatar';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import { red } from '@material-ui/core/colors';
import FavoriteIcon from '@material-ui/icons/Favorite';
import ShareIcon from '@material-ui/icons/Share';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import MoreVertIcon from '@material-ui/icons/MoreVert';
import Button from '@material-ui/core/Button';
import { useStyles } from './HomeStyle.js';
import Grid from '@material-ui/core/Grid';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import Divider from '@material-ui/core/Divider';
import TabPanel from './TabPanel';
import BaseInstance from '../http-client/BaseInstance'
import CourseCreation from './CourseCreation'

export default function RecipeReviewCard(props) {
    const classes = useStyles();
    const [expanded, setExpanded] = React.useState(false);

    var imagePath = props.getPicture()
    if (imagePath === null) imagePath = '../resources/user-icon.png'
    else imagePath = 'http://localhost:8080/signme/' + imagePath

    const onFileChangeHandler = (e) => {
        e.preventDefault();
        console.log("upload")
        const formData = new FormData();
        var nameFile = e.target.files[0].name;
        var extension = nameFile.match(/.*\.(\w+)/)[1];
        nameFile = props.getUser() + "." + extension.toLowerCase();
        formData.append('file', e.target.files[0], nameFile);
        BaseInstance.post("updateProfilePicture", formData)
            .then(res => {
                console.log(res.data);
                if (res.data === "Wrong extension") {
                    alert("wrong extension")
                    return
                }
                if (res.data === "Failed") {
                    alert("generic error")
                    return
                }
                alert("File uploaded successfully.")
                imagePath = 'http://localhost:8080/signme/' + res.data
            })
    };


    return (
        <div className={classes.profileContainer} style={{ padding: 20 }}>
            <Grid container>
                <Grid item xs={3}>
                    <Card className={classes.pictureCard}>
                        <CardMedia
                            className={classes.media}
                            image={imagePath}
                            title="User Icon"
                        />
                        <CardContent>
                            <Button component="label">
                                Update Pic
                                    <input type="file" style={{ display: "none" }} onChange={onFileChangeHandler}
                                    accept=".jpg,.jpeg,.png,.bmp" />
                            </Button>
                        </CardContent>
                    </Card>
                    <Card className={classes.summaryCard}>
                    <div style={{display:"flex","justify-content": "space-between"}}>
                        <Typography className={classes.summaryField}>Name: </Typography> 
                        <Typography className={classes.summaryField}>{props.getCredentials().firstName}</Typography> 
                    </div>
                    <Divider></Divider>
                    <div style={{display:"flex","justify-content": "space-between"}}>
                        <Typography className={classes.summaryField}>Surname: </Typography> 
                        <Typography className={classes.summaryField}>{props.getCredentials().lastName}</Typography> 
                    </div>
                    <Divider></Divider>
                    <div style={{display:"flex","justify-content": "space-between"}}>
                        <Typography className={classes.summaryField}>Email: </Typography> 
                        <Typography className={classes.summaryField}>{props.getCredentials().email}</Typography> 
                    </div>
                    </Card>
                    <CourseCreation getUser={props.getUser} addCourse={props.addCourse} />
                </Grid>

                <Grid item xs={9} style={{ paddingLeft: "20px" }}>
                    <Card className={classes.coursesCard}>
                        <TabPanel getCourses={props.getCourses} getSubscribedCourses={props.getSubscribedCourses} goToCoursePage={props.goToCoursePage} />
                    </Card>
                </Grid>
            </Grid>
        </div>
    );
}