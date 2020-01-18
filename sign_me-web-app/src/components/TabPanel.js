import React from 'react';
import PropTypes from 'prop-types';
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';
import Box from '@material-ui/core/Box';
import CreatedCoursesList from './CreatedCoursesList.js'
import SubscribedCoursesList from './SubscribedCoursesList.js'
import {useStyles} from './TabsStyle.js';

function TabPanel(props) {
  const { children, value, index, ...other } = props;

  return (
    <Typography
      component="div"
      role="tabpanel"
      hidden={value !== index}
      id={`simple-tabpanel-${index}`}
      aria-labelledby={`simple-tab-${index}`}
      {...other}
    >
      {value === index && <Box p={3}>{children}</Box>}
    </Typography>
  );
}

TabPanel.propTypes = {
  children: PropTypes.node,
  index: PropTypes.any.isRequired,
  value: PropTypes.any.isRequired,
};

function a11yProps(index) {
  return {
    id: `simple-tab-${index}`,
    'aria-controls': `simple-tabpanel-${index}`,
  };
}

export default function SimpleTabs(props) {
  const classes = useStyles();
  const [value, setValue] = React.useState(0);

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  return (
    <div className={classes.root}>
      <AppBar position="static" style={{"background-color": "#009569", "box-shadow": "-4px 15px 15px -5px #3E3E3E"}}>
        <Tabs value={value} onChange={handleChange} aria-label="simple tabs example" indicatorColor="Primary">
          <Tab label="Subscribed Courses" {...a11yProps(0)} />
          <Tab label="Created Courses" {...a11yProps(1)} />
        </Tabs>
      </AppBar>
      <TabPanel value={value} index={0}>
        <SubscribedCoursesList/>
      </TabPanel>
      <TabPanel value={value} index={1}>
        <CreatedCoursesList courses={props.courses} setCourses={props.setCourses}/>
      </TabPanel>
    </div>
  );
}