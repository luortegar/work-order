import { Label } from 'src/components/label';
import { SvgColor } from 'src/components/svg-color';

// ----------------------------------------------------------------------

const icon = (name: string) => <SvgColor src={`/assets/icons/navbar/${name}.svg`} />;

export type NavItem = {
  title: string;
  path: string;
  icon: React.ReactNode;
  info?: React.ReactNode;
};


//https://www.svgrepo.com/collection/solar-bold-duotone-icons/
export const navData = [
  {
    title: 'Dashboard',
    path: '/home',
    icon: icon('ic-analytics'),
  },
  {
    title: 'Client',
    path: '/home/client',
    icon: icon('ic-client'),
  },
  {
    title: 'Work order',
    path: '/home/work-order',
    icon: icon('ic-work-order'),
  },
  {
    title: 'User',
    path: '/home/user',
    icon: icon('ic-user'),
  },
  /*
  {
    title: 'Product',
    path: '/products',
    icon: icon('ic-cart'),
    info: (
      <Label color="error" variant="inverted">
        +3
      </Label>
    ),
  },
  /*
  {
    title: 'Blog',
    path: '/blog',
    icon: icon('ic-blog'),
  },
  {
    title: 'Sign in',
    path: '/sign-in',
    icon: icon('ic-lock'),
  },
  
  {
    title: 'Not found',
    path: '/404',
    icon: icon('ic-disabled'),
  },
  */
];
